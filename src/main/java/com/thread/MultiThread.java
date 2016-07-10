package com.thread;

public class MultiThread {

	private static int num = 0;

	//�����ϼ�synchronized�ؼ��� �Ƕ������������Ƕ��� �����Ƿ���������������������������Ӱ��
	public  static synchronized void printNum(String tag) {
		//�༶���� ��ռclass�� һ����˳��ִ��
//		synchronized (MultiThread.class) {
			try {
				if ("a".equals(tag)) {
					num = 100;
					System.out.println("tag a,set num over");
					Thread.sleep(1000);
				}else{
					num=200;
					System.out.println("tag b,set num over");
				}
				System.out.println("tag:"+tag +",num="+num);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//		}
	}
	
	public static void main(String[] args) {
		final MultiThread thread1 = new MultiThread();
		final MultiThread thread2 = new MultiThread();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				thread1.printNum("a");
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				thread2.printNum("b");
			}
		});
		
		t1.start();
		t2.start();
	}
}
