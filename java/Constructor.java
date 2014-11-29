
/**
* 展示构造函数的具体例子
* 
* 示例-关键字-super()的使用
* 示例-关键字-this()的使用
* 示例-构造函数的重载
* 示例-初始化业务逻辑字段的例子
*/	
public class Constructor {
	public static void main(String[] args) {
		
	}
}

class Order{

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static final String IS_NOT_DELETED = "0";

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
		super();
		this.dealDate = SDF.format(new Date());
		this.status = SystemConstant.Mall.CODE_ORDER_WAIT_PAY;
		this.isDeleted = IS_NOT_DELETED;
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
		this();
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
	 * @param buyerMessage 买家留言
	 */
	public Order(List<String> productIds,List<String> amounts,List<String> productTypeIds,long buyerId,String buyerMessage){
		this();
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
		this.buyerMessage = buyerMessage;
	}
}
	