package rpc_pck;

public class InterfaceImplementation implements CodeInterface{

	@Override
	public int add(int a, int b,int c,int d,int e) {
		return a+b+c+d+e;
	}

	@Override
	public int sub(int a, int b) {
		// TODO Auto-generated method stub
		return a-b;
	}

	@Override
	public int mul(int a, int b) {
		// TODO Auto-generated method stub
		return a*b;
	}

	@Override
	public double mul(int a, double b) {
		// TODO Auto-generated method stub
		return a*b;
	}

	@Override
	public double devide(double a, double b) {
		// TODO Auto-generated method stub
		if(b!=0)
			return a/b;
		else
			return -1;
	}
	
}
