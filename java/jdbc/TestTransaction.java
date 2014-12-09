public class TestTransaction{

	public void startTransaction(){

		Connection con = DBCManager.getConnect();//获取连接对象
		
		try {
			con.setAutoCommit(false);//设置事务的提交方式为非自动提交：

			String deleteSql = "delete from me where id = 7";
			String updateSql = "update me set name ='chengong' ,age ='34' where id =4";

			PreparedStatement pStatement = con.prepareStatement(deleteSql);
			pStatement.executeUpdate();

			pStatement = con.prepareStatement(updateSql);
			pStatement.executeUpdate();

			con.commit();
		} catch (SQLException e) {

			e.printStackTrace();
			con.rollback();
		}finally{

			con.setAutoCommit(true);
			DBCManager.release(rs, pStatement, con);
		}
	}
}