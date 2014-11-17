package cn.com.centrin.model.mall;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.centrin.model.EnterpriseUser;

/**
 * Hibernate实体类示例
 * 
 * 实体类应该包括:
 * 示例-serialVersionUID
 * 示例-与数据库对应的属性列和getter setter方法
 * 示例-toString方法
 * 示例-常用构造函数重载
 * @author CuiNan
 *
 */
public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8404887592658346123L;
	
	private String id;
	private String status;//订单状态
	private String refundStatus;//退款状态
	private String dealDate;//创建日期
	private String payDate;//付款日期
	private String dispatchDate;//发货日期
	private String completeDate;//交易完成日期
	private String buyerMessage;//买家留言
	private String orderPrice;//订单价格
	private String refundReason;//退款理由
	private String refuseReason;//拒绝退款理由
	private String cancelReason;//取消订单理由
	private String isDeleted;//假删除字段
	
	private EnterpriseUser seller;//卖家
	private EnterpriseUser buyer;//买家
	
	private Set<OrderProductMapInfo> products;//订单所订购的n种商品集合,每种商品的订购数量也在其中
	
	public Order(){
	}
	
	/**
	 * 创建订单-购买单个商品
	 * 
	 * @param productId 希望购买的产品编号
	 * @param amount 希望购买的产品数量
	 * @param productTypeId 希望购买的商品子类别编号
	 * @param buyerId 商品的购买者
	 */
	public Order(String productId,String amount,String productTypeId,long buyerId){
		
		this.products = new HashSet<OrderProductMapInfo>();
		
		Product product = new Product();
		product.setId(productId);
		ProductType type = new ProductType();
		type.setId(productTypeId);
		
		OrderProductMapInfo orderAndProduct = new OrderProductMapInfo();
		orderAndProduct.setProduct(product);
		orderAndProduct.setAmount(amount);
		orderAndProduct.setProductType(type);
		
		this.products.add(orderAndProduct);
		
		EnterpriseUser buyer = new EnterpriseUser();
		buyer.setId(buyerId);
		this.buyer = buyer;
	}
	
	/**
	 * 创建订单-购买多个商品
	 * 
	 * @param productIds 希望购买的产品编号列表
	 * @param amounts 希望购买的产品数量列表,应该和产品编号列表有对应关系
	 * @param productTypeIds 希望购买的商品子类别编号列表,应该和产品编号列表有对应关系
	 * @param buyerId 商品的购买者
	 */
	public Order(List<String> productIds,List<String> amounts,List<String> productTypeIds,long buyerId){
		
		this.products = new HashSet<OrderProductMapInfo>();
		for(int i = 0 ; i<productIds.size() ; i++){
			
			Product product = new Product();
			product.setId(productIds.get(i));
			ProductType type = new ProductType();
			type.setId(productTypeIds.get(i));
			
			OrderProductMapInfo orderAndProduct = new OrderProductMapInfo();
			orderAndProduct.setProduct(product);
			orderAndProduct.setAmount(amounts.get(i));
			orderAndProduct.setProductType(type);

			this.products.add(orderAndProduct);
		}
		
		EnterpriseUser buyer = new EnterpriseUser();
		buyer.setId(buyerId);
		this.buyer = buyer;
	}
	
	//////////////////////////////////////////////getter and setter ////////////////////////////////////////////
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getDispatchDate() {
		return dispatchDate;
	}
	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public EnterpriseUser getSeller() {
		return seller;
	}
	public void setSeller(EnterpriseUser seller) {
		this.seller = seller;
	}
	public EnterpriseUser getBuyer() {
		return buyer;
	}
	public void setBuyer(EnterpriseUser buyer) {
		this.buyer = buyer;
	}
	
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Set<OrderProductMapInfo> getProducts() {
		return products;
	}
	public void setProducts(Set<OrderProductMapInfo> products) {
		this.products = products;
	}
	@Override
	public String toString() {
		return "Order [buyer=" + buyer + ", buyerMessage=" + buyerMessage
				+ ", cancelReason=" + cancelReason + ", completeDate="
				+ completeDate + ", dealDate=" + dealDate + ", dispatchDate="
				+ dispatchDate + ", id=" + id + ", isDeleted=" + isDeleted
				+ ", orderPrice=" + orderPrice + ", payDate=" + payDate
				+ ", products=" + products + ", refundReason=" + refundReason
				+ ", refundStatus=" + refundStatus + ", refuseReason="
				+ refuseReason + ", seller=" + seller + ", status=" + status
				+ "]";
	}
	
	
}
