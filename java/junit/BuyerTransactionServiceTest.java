package cn.com.centrin.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.JunitTestWithSrping;
import cn.com.centrin.common.SystemConstant;
import cn.com.centrin.dao.IAuditDAO;
import cn.com.centrin.dao.IOrderDAO;
import cn.com.centrin.dto.OrderDTO;
import cn.com.centrin.dto.ProductDTO;
import cn.com.centrin.model.EnterpriseUser;
import cn.com.centrin.model.mall.Order;
import cn.com.centrin.model.mall.OrderProductMapInfo;
import cn.com.centrin.model.mall.Product;
import cn.com.centrin.model.mall.ProductCollect;
import cn.com.centrin.service.IBuyerTransactionService;
import cn.com.centrin.service.IMallSupportService;

/**
 * 商城 - 买家业务逻辑测试类
 * @author CuiNan
 *
 */
public class BuyerTransactionServiceTest extends JunitTestWithSrping{
	
	@Autowired 
	private IBuyerTransactionService buyerTransactionService;
	@Autowired 
	private IMallSupportService mallSupportService;
	@Autowired 
	private IOrderDAO orderDAO;
	@Autowired 
	private IAuditDAO auditDAO;
	
	private static final String PRODUCT_ID = "2c90b8f2490da37001490da3731f0001"; 
//	private static String ORDER_ID = "2c90b8f148f939a50148f939a7e60001"; 
	private static String ORDER_ID = "2c90b8f24912639a01491263a4e20001"; 
	
	private static final OrderDTO orderQuery = new OrderDTO();//查询订单列表所使用的查询条件
	static{
		orderQuery.setOrderByDealDate(true);
		orderQuery.setCurrentPage(1);
		orderQuery.setPageSize(Integer.MAX_VALUE);
	}
	private static Order newOrderInfo1 = new Order();//新建订单时所使用的订单信息
	private static Order newOrderInfo2 = new Order();//新建订单时所使用的订单信息
	private static Order newOrderInfo3 = new Order();//新建订单时所使用的订单信息
	static{
		//初始化1
		Product p = new Product();
		p.setId(PRODUCT_ID);
		Set<OrderProductMapInfo> products = new HashSet<OrderProductMapInfo>();
		OrderProductMapInfo opInfo = new OrderProductMapInfo();
		opInfo.setProduct(p);
		products.add(opInfo);
		newOrderInfo1.setProducts(products);
		
		EnterpriseUser u = new EnterpriseUser();
		u.setId(78L);
		newOrderInfo1.setBuyer(u);
		u = new EnterpriseUser();
		u.setId(79L);
		newOrderInfo1.setSeller(u);
		
		newOrderInfo1.setBuyerMessage("测试用啦啦啦");
		//初始化2
		p = new Product();
		p.setId("2c90b8f2490dd3b401490dd3bdea0001");
		products = new HashSet<OrderProductMapInfo>();
		opInfo = new OrderProductMapInfo();
		opInfo.setProduct(p);
		products.add(opInfo);
		newOrderInfo2.setProducts(products);
		
		u = new EnterpriseUser();
		u.setId(79L);
		newOrderInfo2.setBuyer(u);
		u = new EnterpriseUser();
		u.setId(78L);
		newOrderInfo2.setSeller(u);
		
		newOrderInfo2.setBuyerMessage("测试用啦啦啦");
		//初始化3
		p = new Product();
		p.setId("2c90b8f2490dd3b401490dd3bdea0001");
		products = new HashSet<OrderProductMapInfo>();
		opInfo = new OrderProductMapInfo();
		opInfo.setProduct(p);
		products.add(opInfo);
		newOrderInfo3.setProducts(products);
		
		u = new EnterpriseUser();
		u.setId(79L);
		newOrderInfo3.setBuyer(u);
		u = new EnterpriseUser();
		u.setId(78L);
		newOrderInfo3.setSeller(u);
		
		newOrderInfo3.setBuyerMessage("测试用啦啦啦");
	}
	
	private static OrderDTO orderInfo = new OrderDTO();
	static{
		orderInfo.setId(ORDER_ID);
		orderInfo.setCancelReason("取消订单理由");
		orderInfo.setRefundReason("退款理由");
		orderInfo.setRefuseReason("拒绝退款理由");
	}
	
//	private void ready(){
//		this.buyerTransactionService.buy(newOrderInfo1);
//		ORDER_ID = newOrderInfo1.getId();// 为接下来的测试添加临时的测试用订单ID
//	}
//	private void back(){
//		this.orderDAO.delelte(newOrderInfo1);//删除测试用临时订单信息
//	}
	
	@Test
	public void testBuyOrder() { 
		
//		int orderCountBefore = this.mallSupportService.getOrderList(orderQuery).resultList.size();
		
//		assertEquals(true,this.buyerTransactionService.buy(newOrderInfo1));
		
		Order o = new Order(PRODUCT_ID, "45", "", 78L, "实参固定个数优先");
		assertEquals(true,this.buyerTransactionService.buy(o));
		
		List<String> product = new ArrayList<String>();
		product.add(PRODUCT_ID);
		product.add("2c90b8f2490dd3b401490dd3bdea0001");
		List<String> amount = new ArrayList<String>();
		amount.add("12");
		amount.add("23");
		List<String> type = new ArrayList<String>();
		type.add("");
		type.add("");
		
		Order o2 = new Order(product,amount,type, 78L, "aaa");
		assertEquals(true,this.buyerTransactionService.buy(o2));
		
//		int orderCountAfter = this.mallSupportService.getOrderList(orderQuery).resultList.size();
//		Order newDetail = this.mallSupportService.getOrderDetail(newOrderInfo1.getId());
//		
//		assertEquals(1,orderCountAfter - orderCountBefore);//检查是否新建了订单
//		assertEquals(newOrderInfo1.getBuyer().getId(),newDetail.getBuyer().getId());
//		assertEquals(newOrderInfo1.getSeller().getId(),newDetail.getSeller().getId());
//		assertEquals(((OrderProductMapInfo) newOrderInfo1.getProducts().toArray()[0]).getProduct().getId(),((OrderProductMapInfo) newDetail.getProducts().toArray()[0]).getProduct().getId());
//		
//		this.orderDAO.delelte(newOrderInfo1);
	}

	@Test
	public void testBuyListOfOrder() {
		
		int before = this.mallSupportService.getOrderList(orderQuery).resultList.size();
		
		List<Order> list = new ArrayList<Order>();
		list.add(newOrderInfo2);
		list.add(newOrderInfo3);
		
		this.buyerTransactionService.buy(list);
		int after = this.mallSupportService.getOrderList(orderQuery).resultList.size();
		
		assertEquals(2,after - before);
		
		this.orderDAO.delelte(list.get(0));//回滚
		this.orderDAO.delelte(list.get(1));
	}

	@Test
	public void testCancel() {
		Order beforeStatus = this.mallSupportService.getOrderDetail(ORDER_ID);
		
		this.buyerTransactionService.cancel(orderInfo);
		Order detail = this.mallSupportService.getOrderDetail(ORDER_ID);
		assertEquals(SystemConstant.Mall.CODE_ORDER_CANCEL, detail.getStatus());
		
		Order o = new Order();
		o.setId(beforeStatus.getId());
		o.setStatus(beforeStatus.getStatus());
		this.orderDAO.modityOrder(o);//回滚
	}

	@Test
	public void testCollect() {
		ProductDTO dto = new ProductDTO();
		dto.setUserId("78");
		dto.setCurrentPage(1);
		dto.setPageSize(100);
		dto.setOrderByPrice(true);
		
		int before = this.buyerTransactionService.getCollectList(dto).resultList.size();
		
		ProductCollect collect = new ProductCollect();
		Product p = new Product();
		p.setId(PRODUCT_ID);
		collect.setProduct(p);
		collect.setUserId("78");
		this.buyerTransactionService.collect(collect);
		
		int after = this.buyerTransactionService.getCollectList(dto).resultList.size();
		
		assertEquals(1,after - before);
	}

	@Test
	public void testDeleteCollectProductCollect() {
		ProductDTO dto = new ProductDTO();
		dto.setUserId("78");
		dto.setCurrentPage(1);
		dto.setPageSize(100);
		dto.setOrderByPrice(true);
		
		int before = this.buyerTransactionService.getCollectList(dto).getResultList().size();
		
		ProductCollect collect = new ProductCollect();
		Product p = new Product();
		p.setId(PRODUCT_ID);
		collect.setProduct(p);
		collect.setUserId("78");
		this.buyerTransactionService.deleteCollect(collect);
		
		int after = this.buyerTransactionService.getCollectList(dto).getResultList().size();
		
		assertEquals(-1,after - before);
	}

	@Test
	public void testDeleteCollectListOfProductCollect() {
		List<ProductCollect> list = new ArrayList<ProductCollect>();
		ProductDTO dto = new ProductDTO();
		dto.setUserId("78");
		dto.setCurrentPage(1);
		dto.setPageSize(100);
		dto.setOrderByPrice(true);
		
		int before = this.buyerTransactionService.getCollectList(dto).getResultList().size();
		
		ProductCollect collect = new ProductCollect(PRODUCT_ID, "78");
		this.buyerTransactionService.collect(collect);
		list.add(collect);
		collect = new ProductCollect(PRODUCT_ID, "78");
		this.buyerTransactionService.collect(collect);
		list.add(collect);
		
		this.buyerTransactionService.deleteCollect(list);
		
		int after = this.buyerTransactionService.getCollectList(dto).getResultList().size();
		assertEquals(0, after - before);
		
	}

	@Test
	public void testPay() {
		Order beforeStatus = this.mallSupportService.getOrderDetail(ORDER_ID);
		
		this.buyerTransactionService.pay(orderInfo);
		Order detail = this.mallSupportService.getOrderDetail(ORDER_ID);
		assertEquals(SystemConstant.Mall.CODE_ORDER_PAID, detail.getStatus());
		
		this.orderDAO.modityOrder(beforeStatus);
	}

	@Test
	public void testReceive() {
		Order beforeStatus = this.mallSupportService.getOrderDetail(ORDER_ID);
		
		this.buyerTransactionService.receive(orderInfo);
		Order detail = this.mallSupportService.getOrderDetail(ORDER_ID);
		assertEquals(SystemConstant.Mall.CODE_ORDER_COMPLETE, detail.getStatus());
		
		this.orderDAO.modityOrder(beforeStatus);
	}

	@Test
	public void testRefund() {
		Order beforeStatus = this.mallSupportService.getOrderDetail(ORDER_ID);
		
		this.buyerTransactionService.refund(orderInfo);
		Order detail = this.mallSupportService.getOrderDetail(ORDER_ID);
		assertEquals(SystemConstant.Mall.CODE_ORDER_REFUNDING, detail.getRefundStatus());
		
		this.orderDAO.modityOrder(beforeStatus);
	}


	@Test
	public void testLeaveMessage() {
		Order beforeStatus = this.mallSupportService.getOrderDetail(ORDER_ID);
		
		OrderDTO order = new OrderDTO();
		order.setId(ORDER_ID);
		order.setBuyerMessage("xxx");
		
		this.buyerTransactionService.leaveMessage(order);
		
		Order detail = this.mallSupportService.getOrderDetail(ORDER_ID);
		assertEquals("xxx", detail.getBuyerMessage());
		
		this.orderDAO.modityOrder(beforeStatus);
		
	}
	
	@Test
	public void testAskHelp() {
		fail("Not yet implemented");
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public IBuyerTransactionService getBuyerTransactionService() {
		return buyerTransactionService;
	}

	public void setBuyerTransactionService(
			IBuyerTransactionService buyerTransactionService) {
		this.buyerTransactionService = buyerTransactionService;
	}

	public IMallSupportService getMallSupportService() {
		return mallSupportService;
	}

	public void setMallSupportService(IMallSupportService mallSupportService) {
		this.mallSupportService = mallSupportService;
	}

	public IOrderDAO getOrderDAO() {
		return orderDAO;
	}

	public void setOrderDAO(IOrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public IAuditDAO getAuditDAO() {
		return auditDAO;
	}

	public void setAuditDAO(IAuditDAO auditDAO) {
		this.auditDAO = auditDAO;
	}

}
