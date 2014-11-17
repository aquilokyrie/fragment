package cn.com.centrin.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.centrin.common.BaseDAO;
import cn.com.centrin.common.Page;
import cn.com.centrin.common.StringUtil;
import cn.com.centrin.common.SystemConstant;
import cn.com.centrin.dao.IVideoCloudDAO;
import cn.com.centrin.model.UserIdAndName;
import cn.com.centrin.model.UsersAndVideos;
import cn.com.centrin.model.VideoCloudDeletedVideos;
import cn.com.centrin.model.VideoCloudSpaceManage;
import cn.com.centrin.model.VideoCloudSpaceManageAndUser;

/**
* 中金出差期间的dao
*
* 示例-hql-删除
* 示例-sql-列表查询
* 示例-hibernate函数式查询
* 示例-hibernate回调
* 示例-hibernate中比较复杂的sql查询
*/
@SuppressWarnings("serial")
public class VideoCloudDAO extends BaseDAO implements IVideoCloudDAO {
	
	/**
	* 使用hql删除实体
	* @param userId 用户编号,实体的主键
	*/
	public void deleteSpaceManage(int userId) {

		String hql = "delete from VideoCloudSpaceManage where userId = " + userId;
		this.getSession().createQuery(hql).executeUpdate();
	}
	
	/**
	* hibernate函数式查询->DTO列表
	* hibernate回调
	* 
	* 一般来说,输入参数应该是DTO类,以避免查询条件增加时对接口的修改
	* 
	* @param userId 		查询条件-视频拥有者用户编号
	* @param videoName 		查询条件-视频名
	* @param status 		查询条件-视频状态
	* @param beginDate 		查询条件-起始日期
	* @param endDate 		查询条件-结束日期
	* @param currentPage 	分页-当前页
	* @param pageSize 		分页-页大小
	* @return 实体类的Page
	*/
	@SuppressWarnings("unchecked")
	public Page<UsersAndVideos> findDeletedVideos(
		Integer userId,
		String videoName, 
		int status,
		final int currentPage,
		final int pageSize,
		String beginDate, 
		String endDate) {

		final Page<UsersAndVideos> page = new Page<UsersAndVideos>();
		final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UsersAndVideos.class);
		try {
			// 设置查询条件
			detachedCriteria.add(Restrictions.eq("userId", userId));
			// 视频名
			if (!StringUtil.isEmptyAfterTrim(videoName))
				detachedCriteria.add(Restrictions.like("videoName", videoName.trim(), MatchMode.ANYWHERE));
			//日期区间
			if (!StringUtil.isEmptyAfterTrim(beginDate))
				detachedCriteria.add(Restrictions.ge("date", beginDate.trim()));
			//日期区间,当数据库中日期以字符串形式存储
			if (!StringUtil.isEmptyAfterTrim(endDate))
				detachedCriteria.add(Restrictions.le("date", endDate.trim() + " 99:99:99"));
			//视频状态
			if (0 != status) //等于0时查询全部状态的视频
				detachedCriteria.add(Restrictions.eq("videoStatus", status));
			//是否假删除
			detachedCriteria.add(Restrictions.eq("isDeleted",SystemConstant.IS_DELETED ));
			
			return (Page<UsersAndVideos>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {

					Criteria criteria = detachedCriteria.getExecutableCriteria(session);
					//设置排序方式
					criteria.addOrder(Order.desc("completeDate"));
					
					int recordsize = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

					criteria.setProjection(null);
					criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
					List<UsersAndVideos> items = criteria.setCacheable(true).setFirstResult((currentPage-1)*pageSize).setMaxResults(pageSize).list();
					//重新设置分页信息
					page.setTotal(recordsize);			//分页-总记录数
					page.setPageSize(pageSize);			//分页-页大小
					page.setCurrentPage(currentPage);	//分页-当前页
					page.setResultList(items);			
					return page;
				}
			});

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return page;
	}

	/**
	* 在hibernate中使用sql查询实体列表
	* 
	* @param userName 视频拥有者的用户名
	* @return 用户列表,UserIdAndName是因为用户系统在另一个部门开发,为了兼容性我们自己使用的用户实体类
	*/
	@SuppressWarnings("unchecked")
	public List<UserIdAndName> findNoSpaceUsersByUserName(String userName) {

		String sql = "select id,username from t_enterprise_user where username like '%" + userName + "%'";
		SQLQuery query = getSession().createSQLQuery(sql);
		//使用sql查询时需要设置实体class,以保证实体能够正确的包装
		List<UserIdAndName> retList = query.addEntity(UserIdAndName.class).list();
		return retList;
	}
	
	/**
	* hibernate中使用复杂的sql查询
	* hibernate的缺点就是对复杂要求的查询支持较弱
	* 
	* @param manage 查询条件DTO
	* @param currentPage 分页-当前页
	* @param pageSize    分页-页大小
	*/
	@SuppressWarnings("unchecked")
	public Page<VideoCloudSpaceManageAndUser> findSpaceManagePage(VideoCloudSpaceManageAndUser manage, int currentPage, int pageSize) {
		
		String sql = "select " +
				"t.id, " +
				"t.user_id, " +
				"t.username, " +
				"t.space_size, " +
				"t.video_amount, " +
				"t.use_size, " +
				"t.left_size " +
				"from(" +
				"	select " +
				"		t_vcloud_space_manage.id," +
				"		t_vcloud_space_manage.user_id, " +
				"		t_vcloud_space_manage.space_size, " +
				"		t_vcloud_space_manage.use_size, " +
				"		t_enterprise_user.username, " +
				"		t_vcloud_space_manage.video_amount, " +
				"		CAST( CAST(t_vcloud_space_manage.space_size AS DECIMAL ) - CAST(t_vcloud_space_manage.use_size AS DECIMAL) AS  char) as left_size " +
				"	from " +
				"		t_vcloud_space_manage, " +
				"		t_enterprise_user " +
				"	where " +
				"		t_vcloud_space_manage.user_id = t_enterprise_user.id " +
				") t " +
				"where 1=1 " ;
		//添加查询条件
		if(!StringUtil.isEmptyAfterTrim(manage.getUserName()))
			sql += "and t.username like '%"+ manage.getUserName().trim() +"%' ";
		if(!StringUtil.isEmptyAfterTrim(manage.getLeftSize_max()))
			sql += "and CAST(t.left_size AS DECIMAL ) <= CAST('"+ manage.getLeftSize_max().trim() +"' AS DECIMAL ) " ;
		if(!StringUtil.isEmptyAfterTrim(manage.getLeftSize_min()))
			sql += "and CAST(t.left_size AS DECIMAL ) >= CAST('"+ manage.getLeftSize_min().trim() +"' AS DECIMAL ) ";
		if(!StringUtil.isEmptyAfterTrim(manage.getSpaceSize_max()))
			sql += "and CAST(t.space_size AS DECIMAL ) <= CAST('"+ manage.getSpaceSize_max().trim() +"' AS DECIMAL ) ";
		if(!StringUtil.isEmptyAfterTrim(manage.getSpaceSize_min()))
			sql += "and CAST(t.space_size AS DECIMAL ) >= CAST('"+ manage.getSpaceSize_min().trim() +"' AS DECIMAL ) ";
		if(!StringUtil.isEmptyAfterTrim(manage.getUseSize_max()))
			sql += "and CAST(t.use_size AS DECIMAL ) <= CAST('"+ manage.getUseSize_max().trim() +"' AS DECIMAL ) ";
		if(!StringUtil.isEmptyAfterTrim(manage.getUseSize_min()))
			sql += "and CAST(t.use_size AS DECIMAL ) >= CAST('"+ manage.getUseSize_min().trim() +"' AS DECIMAL ) ";
		if(manage.getVideoAmount_max() >= 0)
			sql += "and t.video_amount <= "+ manage.getVideoAmount_max() +" ";
		if(manage.getVideoAmount_min() >= 0)
			sql += "and t.video_amount >= "+ manage.getVideoAmount_min() +" " ;
		
		sql += "order by t.username,t.space_size,t.video_amount,t.use_size,t.left_size ";
		
		SQLQuery query = getSession().createSQLQuery(sql);
		List<VideoCloudSpaceManageAndUser> retList = query.addEntity(VideoCloudSpaceManageAndUser.class).list();
		//分页
		Page<VideoCloudSpaceManageAndUser> page = new Page<VideoCloudSpaceManageAndUser>();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotal(retList.size());
		if(currentPage*pageSize<retList.size())
			page.setResultList(retList.subList( (currentPage-1)*pageSize , currentPage*pageSize) );
		else
			page.setResultList(retList.subList( (currentPage-1)*pageSize , retList.size() ) );
		
		return page;
	}
		
}
