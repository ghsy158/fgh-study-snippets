package com.thread;

/**
 * 脏读
 * @author fgh
 * @Since 2016年3月13日 下午4:45:14
 */
public class DirtyRead {

	private String userName =  "fengguanghui";
	
	private String password ="123456";
	
	public  synchronized void setValue(String userName,String password){
		this.userName = userName;
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.password = password;
		
		System.out.println("setValue结果:userName:"+this.userName +",password="+this.password);
	}
	
	//synchronized
	public  synchronized void getValue(){
		System.out.println("getValue结果:userName:"+this.userName +",password="+this.password);
	}
	
	public static void main(String[] args) throws InterruptedException {
		final DirtyRead dr = new DirtyRead();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				dr.setValue("lisi", "6666");
			}
		});
		t1.start();
		
		Thread.sleep(1000);
		
		dr.getValue();
	}
}
