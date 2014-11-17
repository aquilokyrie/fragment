package cn.com.centrin.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.com.centrin.common.Page;
import cn.com.centrin.common.StringUtil;
import cn.com.centrin.common.SystemConstant;
import cn.com.centrin.dao.IProductDAO;
import cn.com.centrin.dto.ProductDTO;
import cn.com.centrin.model.mall.Product;
import cn.com.centrin.model.mall.ProductCategory;
import cn.com.centrin.model.mall.ProductHistoryPrice;
import cn.com.centrin.model.mall.ProductType;

/**
 * 使用HibernateDaoSupport类来进行数据操作示例
 * 
 * HibernateDaoSupport-获取Session
 * HibernateTemplate-添加
 * HibernateTemplate-删除
 * HibernateTemplate-修改
 * HibernateTemplate-单个实体查询
 * 修改示例-每个属性列都应该可改
 * 列表查询示例-充分的查询条件
 * 
 * @author CuiNan
 *
 */
public class CopyOfProductDAO extends HibernateDaoSupport implements IProductDAO {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 使用HibernateTemplate进行数据库添加的示例
	 * 
	 * @param product 希望添加的实体信息
	 */
	public String create(Product product) {
		
		if(product != null)
			this.getHibernateTemplate().save(product);
		return product.getId();
	}

	/**
	 * 示例-使用HibernateTemplate进行数据删除
	 * 示例-使用HibernateDaoSupport获取session
	 * 示例-使用Session执行sql
	 * 
	 * @param product 希望删除的实体信息,此处使用了记录主键
	 */
	public void delete(Product product) {

		if(product != null)
			this.getHibernateTemplate().delete(product);
		//hibernate只会把一对多关联的多的无效外键置空,而不会真删除记录
		String sql = "delete from t_mall_product_history_price where product_id is null";
		this.getSession().createSQLQuery(sql).executeUpdate();
		sql = "delete from t_mall_product_type where product_id is null";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	/**
	 * 示例-使用HibernateTemplate进行单个实体查询
	 * 
	 * @param productId 商品编号,实体的主键
	 */
	public Product getProduct(String productId) {
		
		if(StringUtil.isEmptyAfterTrim(productId))
			return null;
		else return (Product) this.getHibernateTemplate().get(Product.class, productId);
	}

	/**
	 * 列表查询示例
	 * 提供完备的查询条件,排序方法,分页功能
	 * 
	 * @param query 查询条件DTO
	 */
	@SuppressWarnings("unchecked")
	public Page<Product> getProductList(ProductDTO query) {
		String hql = "select distinct p from Product as p where 1=1 ";
		
		//设置查询条件
		if(!StringUtil.isEmptyAfterTrim(query.getName()))
			hql += " and p.name like '%" + query.getName() + "%' ";
		if(!StringUtil.isEmptyAfterTrim(query.getMinPrice()))
			hql += " and p.price >= " + query.getMinPrice() + " ";		//此处不加引号',作为数值型为查询条件,能自动转型作为数字参与比较
		if(!StringUtil.isEmptyAfterTrim(query.getMaxPrice()))
			hql += " and p.price <= " + query.getMaxPrice() + " ";		//此处不加引号',作为数值型为查询条件,能自动转型作为数字参与比较
		if(!StringUtil.isEmptyAfterTrim(query.getSellerName()))
			hql += " and p.seller.name like '%" + query.getSellerName() + "%' ";		//供货商姓名
		if(!StringUtil.isEmptyAfterTrim(query.getCompanyName()))
			hql += " and p.seller.companyName like '%" + query.getCompanyName() + "%' "; //供货商公司名
		if(!StringUtil.isEmptyAfterTrim(query.getUserId()))
			hql += " and p.seller.id = " + query.getUserId() + " ";		//供货商编号
		if((!StringUtil.isEmptyAfterTrim(query.getIsRecommend())) && query.getIsRecommend().equals(SystemConstant.Mall.IS_RECOMMEND))
			hql += " and p.isRecommend = '" + SystemConstant.Mall.IS_RECOMMEND + "' ";	//是否只查询被推荐的商品
		if(!StringUtil.isEmptyAfterTrim(query.getStatus()))
			hql += " and p.status = '" + query.getStatus() + "' ";		//只查询特定状态的商品
		
		if(!StringUtil.isEmptyAfterTrim(query.getCategoryId())){		//商品类别及其子类别
			String  cataHql = "from ProductCategory as c where c.parent = '" + query.getCategoryId() + "'";
			List<ProductCategory> cList = this.getSession().createQuery(cataHql).list();
			
			hql += " and (p.categoryId = '" + query.getCategoryId() + "' ";	
			
			for(ProductCategory cata : cList){
				hql += " or p.categoryId = '" + cata.getId() + "' ";
			}
			
			hql += " ) ";
		}
		
		//设置排序方式
		hql += " order by ";
	
		String orderStyle = "";
		if(query.isOrderByPrice())
			orderStyle += " ,ABS(p.price) " ;
		if(query.isOrderByClicks())
			orderStyle += " ,p.clicks ";
		if(query.isOrderByParchase())
			orderStyle += " ,p.parchase ";
		if(query.isOrderByShelfOnDate())
			orderStyle += " ,p.shelfOnDate ";
		if(!query.isOrderByPrice() &&
				!query.isOrderByClicks() &&
				!query.isOrderByParchase() &&
				!query.isOrderByShelfOnDate()
		)
			hql += ",p.shelfOnDate ";
		
		hql += orderStyle.replaceFirst(",", " ");
		if(!StringUtil.isEmptyAfterTrim(query.getOrderType()))
			hql += query.getOrderType();//降序升序
		else
			hql += " desc ";
		
		//设置分页信息
		if(query.getCurrentPage() == 0 || query.getPageSize() == 0){
			query.setCurrentPage(1);
			query.setPageSize(20);
		}
		
		Page<Product> page = new Page<Product>();
		Query q = this.getSession().createQuery(hql);
		page.setTotal(q.list().size());
		
		q.setMaxResults(query.getPageSize());
		q.setFirstResult( (query.getCurrentPage() - 1) * query.getPageSize() );
		List<Product> list = (List<Product>)q.list();
		
		page.setPageSize(query.getPageSize());
		page.setCurrentPage(query.getCurrentPage());
		page.setResultList(list);
		
		return page;
	}

	/**
	 * 示例-使用HibernateTemplate进行实体修改
	 * 在dao层,实体的每个属性都应该是可改的
	 * 
	 * @param product 实体的主键和待修改信息,毋需修改的信息在传入实体内置空即可
	 */
	public void modify(Product product) {
		Product oldInfo = this.getProduct(product.getId());//获得旧数据
		
		//检查发生变化的项目并修改
		if(!StringUtil.isEmptyAfterTrim(product.getCategoryId()))
			oldInfo.setCategoryId(product.getCategoryId());
		if(!StringUtil.isEmptyAfterTrim(product.getDiscount()))
			oldInfo.setDiscount(product.getDiscount());
		if(!StringUtil.isEmptyAfterTrim(product.getIntroduction()))
			oldInfo.setIntroduction(product.getIntroduction());
		if(!StringUtil.isEmptyAfterTrim(product.getName()))
			oldInfo.setName(product.getName());
		if(!StringUtil.isEmptyAfterTrim(product.getPicture()))
			oldInfo.setPicture(product.getPicture());
		if(!StringUtil.isEmptyAfterTrim(product.getStatus()))
			oldInfo.setStatus(product.getStatus());
		if(!StringUtil.isEmptyAfterTrim(product.getIsRecommend()))
			oldInfo.setIsRecommend(product.getIsRecommend());
		if(!StringUtil.isEmptyAfterTrim(product.getParchase()))
			oldInfo.setParchase(product.getParchase());
		//修改商品之下的子类别
		if(product.getTypes() != null && !product.getTypes().isEmpty()){
			//删除原有所有子类别,hibernate会自动按照新给出的类别集合全新插入
			for(ProductType type : oldInfo.getTypes())
				this.getHibernateTemplate().delete(type);
			
			oldInfo.setTypes(product.getTypes());
		}
		if(!StringUtil.isEmptyAfterTrim(product.getPrice())){//修改价格并添加历史价格记录
			oldInfo.setPrice(product.getPrice());
			
			ProductHistoryPrice historyPrice = new ProductHistoryPrice();
			historyPrice.setDate(sdf.format(new Date()));
			historyPrice.setPrice(product.getPrice());
			historyPrice.setProductId(product.getId());
			this.getHibernateTemplate().save(historyPrice);
		}
		this.getHibernateTemplate().update(oldInfo);
	}

}
