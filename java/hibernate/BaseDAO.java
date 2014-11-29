package cn.com.centrin.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 中金 dao 基类
 * 
 * @author shengyidong
 * 
 */
@SuppressWarnings("serial")
public class BaseDAO extends HibernateDaoSupport implements Serializable {

	public BaseDAO() {
	}

	/**
	 * 保存对象 新增
	 * 
	 * @param entity
	 * 
	 */
	public void save(final Object entity) {
		getHibernateTemplate().save(entity);
	}

	/**
	 * 新增或更新对象
	 * 
	 * @param entity
	 */
	public void saveOrupdate(final Object entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * 更新对象
	 * 
	 * @param entity
	 */
	public void update(final Object entity) {
		getHibernateTemplate().update(entity);
	}

	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void delete(final Object entity) {
		getHibernateTemplate().delete(entity);
	}

	/**
	 * 根据ID获取对象
	 * 
	 * @param entity
	 * @param id
	 * @return 对象，或者未能发现符合条件的记录，返回null
	 */
	public <T> T get(final Class<T> entity, final Serializable id) {
		return (T) getHibernateTemplate().get(entity, id);
	}

	/**
	 * 取sequence的值
	 * 
	 * @param seqname
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public long getSequence(final String seqname) {
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				// String sql = "select "+seqname+".nextval seqid from dual";
				// oracle使用
				String sql = "SELECT nextval('" + seqname + "') as seqid ";
				SQLQuery query = session.createSQLQuery(sql);
				query.addScalar("seqid", new org.hibernate.type.LongType());
				List list = query.list();
				long sequece = (Long) list.get(0);
				return sequece;
			}
		});
		return ((Long) object).longValue();
	}

	/**
	 * 返回所有对象的列表
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findAll(final Class entity) {
		return getHibernateTemplate().find("from " + entity.getName());
	}

	/**
	 * 
	 * @param query
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByQuery(final String query) {
		return getHibernateTemplate().find(query);
	}

	/**
	 * 
	 * @param query
	 * @param parameter
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByQuery(final String query, final Object parameter) {
		return getHibernateTemplate().find(query, parameter);
	}

	/**
	 * query="from user u where u.username=? and u.password=?" parameter {"tom","123456"}
	 * 
	 * @param query
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findByQuery(final String query, final Object... parameter) {
		return getHibernateTemplate().find(query, parameter);
	}

	@SuppressWarnings("unchecked")
	public List findBySqlQuery(final String sql) {
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query queryObject = session.createSQLQuery(sql);
				return queryObject.list();
			}
		});
		return ((List) object);
	}

	/**
	 * 
	 * 查询指定数量的对象
	 * 
	 * @param query
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findNumList(final String query, final int resultcount) {
		return (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query q = session.createQuery(query);
				q.setMaxResults(resultcount);
				return q.list();
			}
		});
	}

	/**
	 * 返回符合条件的记录列表
	 * 
	 * @param detachedCriteria
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByCriteria(final DetachedCriteria detachedCriteria) {
		return (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				return criteria.list();
			}
		});
	}

	/**
	 * 搜索指定数量的数据
	 * 
	 * @param detachedCriteria
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findByCriteria(final DetachedCriteria detachedCriteria, final int count) {
		return (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY).setMaxResults(count);
				return criteria.list();
			}
		});
	}

	/**
	 * 批量删除
	 * 
	 * @param hql
	 * @param hql
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public int deletes(final String hql, final Object[] ids) {
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				int deletedEntities = session.createQuery(hql).setParameterList("ids", ids).executeUpdate();
				return deletedEntities;
			}
		});
		return ((Integer) object).intValue();
	}

	/**
	 * 执行hql 更新,删除 操作
	 * 
	 * @param hql
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public int execute(final String hql) {
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query queryObject = session.createQuery(hql);
				int i = queryObject.executeUpdate();
				return new Integer(i);
			}
		});
		return ((Integer) object).intValue();
	}

	/**
	 * 执行sql 更新,删除 操作
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int executeSql(final String sql) {
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query queryObject = session.createSQLQuery(sql);
				int i = queryObject.executeUpdate();
				return new Integer(i);
			}
		});
		return ((Integer) object).intValue();
	}

	/**
	 * 分页查询
	 * 
	 * @param detachedCriteria
	 *            条件
	 * @param page
	 *            页面信息 包含 maxperpage pagenumber 等
	 */

	@SuppressWarnings("unchecked")
	public PageSupport findPageByCriteria(final DetachedCriteria detachedCriteria, final PageSupport page) {
		return (PageSupport) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {

				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				int recordsize = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
				// 请不要随便动这里的代码, 如果这里报错 请先检查 配置文件有没有包含hibernate的xml文件
				// 计算记录条数
				// criteria.setProjection(Projections.rowCount());
				// int recordsize = (Integer) criteria.uniqueResult();

				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				List items = criteria.setCacheable(true).setFirstResult(page.getStartRecord()).setMaxResults(page.maxperpage).list();
				PageSupport ps = new PageSupport(page, items, recordsize);
				return ps;

			}
		});
	}

	/**
	 * 返回符合条件的记录数量
	 * 
	 * @param detachedCriteria
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public int getCountByCriteria(final DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				return criteria.setProjection(Projections.rowCount()).uniqueResult();
			}
		});
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	public List listByPage(final String queryString, final int currentPage, final int pageSize, final Object... values) throws DataAccessException {
		return (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public List doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.setFirstResult(currentPage * pageSize).setMaxResults(pageSize).list();
			}
		});
	}

	protected void prepareQuery(Query queryObject) {
		if (getTemplate().isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getTemplate().getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getTemplate().getQueryCacheRegion());
			}
		}
		if (getTemplate().getFetchSize() > 0) {
			queryObject.setFetchSize(getTemplate().getFetchSize());
		}
		if (getTemplate().getMaxResults() > 0) {
			queryObject.setMaxResults(getTemplate().getMaxResults());
		}
		SessionFactoryUtils.applyTransactionTimeout(queryObject, getSessionFactory());
	}

	public HibernateTemplate getTemplate() {
		return this.getHibernateTemplate();
	}
}
