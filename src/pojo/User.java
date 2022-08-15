package pojo;

/**
 * 操作用户
 *
 */
public class User {
	/**
	 * 普通用户权限
	 */
	public final static String GUEST = "guest";// 用在用户信息页面做选择
	/**
	 * 管理员权限
	 */
	public final static String ADMIN = "admin";// 用在用户信息页面做选择

	private int id;// 编号
	private String account;// 账号
	private String password;// 密码
	private String status;// 身份
	private String available;// 是否有效
	private String createTime;// 创建时间
	private String lastUpdateTime;// 最后一次修改时间
	private String operator;// 变更操作人

	public User() {
	}

	public User(String account, String password, String status) {
		super();
		this.account = account;
		this.password = password;
		this.status = status;
		this.available = "Y";
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password="
				+ password + ", status=" + status + ", available=" + available
				+ ", createTime=" + createTime + ", lastUpdateTime="
				+ lastUpdateTime + ", operator=" + operator + "]";
	}

}
