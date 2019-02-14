package rpc_pck;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

public class CodeInterface_ { 
int sub (int a1, int a2)
{
return (int)doCommunication("CodeInterface_@int@sub@2@int@"+a1+"@int@"+a2 ,"127.0.0.1", 9999);}
int mul (int a1, int a2)
{
return (int)doCommunication("CodeInterface_@int@mul@2@int@"+a1+"@int@"+a2 ,"127.0.0.1", 9999);}
double mul (int a1, double a2)
{
return (double)doCommunication("CodeInterface_@double@mul@2@int@"+a1+"@double@"+a2 ,"127.0.0.1", 9999);}
double devide (double a1, double a2)
{
return (double)doCommunication("CodeInterface_@double@devide@2@double@"+a1+"@double@"+a2 ,"127.0.0.1", 9999);}
int add (int a1, int a2, int a3, int a4, int a5)
{
return (int)doCommunication("CodeInterface_@int@add@5@int@"+a1+"@int@"+a2+"@int@"+a3+"@int@"+a4+"@int@"+a5 ,"127.0.0.1", 9999);}
	public static Object doCommunication(String sentString, String addr, int p) {
		String ans = null;
		Socket socket = null;
		DataInputStream readFromServer = null;
		DataOutputStream out = null;
		String address = addr;
		int port = p;
		String[] parts = sentString.split("@");
		String returnType = parts[1];
		// establish a connection
		try {
			socket = new Socket(address, port);

			// takes input from terminal
			readFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}

		try {
			out.writeUTF(sentString);
		} catch (IOException i) {
			System.out.println(i);
		}
		try {
			ans = readFromServer.readUTF();

		} catch (IOException i) {
			System.out.println(i);
		}

		// close the connection
		try {
			readFromServer.close();
			out.close();
			socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
		return conversion(ans, returnType);
	}
public static Object conversion(String value, String type) {
		if (type.equals("int")) {
			return Integer.parseInt(value);
		} else if (type.equals("String")) {
			return value;
		} else if (type.equals("double")) {
			return Double.parseDouble(value);
		} else if (type.equals("float")) {
			return Float.parseFloat(value);
		} else if (type.equals("char")) {
			return value.charAt(0);
		} else if (type.equals("boolean")) {
			return Boolean.parseBoolean(value);
		} else {
			return value;
		}
	}
}