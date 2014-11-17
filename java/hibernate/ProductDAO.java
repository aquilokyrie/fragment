package cn.com.centrin.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import cn.com.centrin.common.BaseDAO;
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
 * 商品-数据库操作实现类
 * hibernate增删改查CRUD的典型实现
 * hibernate列表查询的几种常见情况
 * @author CuiNan
 *
 */
public class ProductDAO extends BaseDAO implements IProductDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3722276050353785919L;
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	* 新建
	*/
	public String create(Product product) {

		if(product != null)
			super.save(product);		
		return product.getId();
	}

	/**
	* 删除
	*/
	public void delete(Product product) {

		if(product != null)
			this.getHibernateTemplate().delete(product);
		
		//hibernate只会把一对多关联的多的无效外键置空,而不会真删除记录
		String sql = "delete from t_mall_product_history_price where product_id is null";
		this.executeSql(sql);
		sql = "delete from t_mall_product_type where product_id is null";
		this.executeSql(sql);
	}

	/**
	* 查询单个实体
	*/
	public Product getProduct(String productId) {
		
		if(StringUtil.isEmptyAfterTrim(productId))
			return null;
		else return this.get(Product.class, productId);
	}

	/**
	* 查询实体列表
	*/
	@SuppressWarnings("unchecked")
	public Page<Product> getProductList(ProductDTO query) {

		//设置默认分页信息
		if(query.getCurrentPage() == 0 || query.getPageSize() == 0){
			query.setCurrentPage(1);
			query.setPageSize(20);
		}

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
	* 修改
	*/
	public void modify(Product product) {
		Product oldInfo = this.getProduct(product.getId());//获得旧数据
		
		if(!StringUtil.isEmptyAfterTrim(product.getCategoryId()))//检查发生变化的项目并修改
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
		
		if(product.getTypes() != null && !product.getTypes().isEmpty()){//修改商品之下的子类别
			//删除原有所有子类别,hibernate会自动按照新给出的类别集合全新插入
			for(ProductType type : oldInfo.getTypes())
				this.getHibernateTemplate().delete(type);
			
			oldInfo.setTypes(product.getTypes());
		}

		if(!StringUtil.isEmptyAfterTrim(product.getPrice())){//修改价格并添加历史价格记录
			oldInfo.setPrice(product.getPrice());
			
			ProductHistoryPrice historyPrice = new ProductHistoryPrice();
			historyPrice.setDate(SDF.format(new Date()));
			historyPrice.setPrice(product.getPrice());
			historyPrice.setProductId(product.getId());
			this.save(historyPrice);
		}
		
		this.update(oldInfo);
	}
	
}
