package rpc_pck;

import java.awt.Window.Type;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerHandler {

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Scanner sc = new Scanner(System.in);
//		String interfaceName = sc.next();
//		String className = sc.next();

		Socket socket = null;
		ServerSocket server = null;
		DataInputStream in = null;
		int port = 9999;
		String line = "";
		// starts server and waits for a connection
		try {
			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted");

			// takes input from the client socket
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			try {
				line = in.readUTF();
				System.out.println(line);

			} catch (IOException i) {
				System.out.println(i);
			}

		} catch (IOException i) {
			System.out.println(i);
		}

		String[] parts = line.split("@");
		String className = parts[0];
		String returnType = parts[1];
		String methodName = parts[2];
		int paramlen = Integer.parseInt(parts[3]);
		String replystr = null;
		Method[] classMethods = CodeInterface.class.getDeclaredMethods();
		ArrayList<Class> type = null;
		Class[] type_array = new Class[paramlen];
		ArrayList<Object> argv = null;
		Object[] argv_array = new Object[paramlen];
		int flag = 0;
		int index = 0;
		for (Method md : classMethods) {
			flag = 0;
			System.out.println(
					methodName + ", " + md.getName() + "  ,  " + returnType + " , " + md.getReturnType().toString());
			if (methodName.equals(md.getName()) && returnType.equals(md.getReturnType().toString())) {
				System.out.println("Inside method & returntype match");
				Class[] paraArray = md.getParameterTypes();
				if (paraArray.length == paramlen) {

//					type = new ArrayList<Class>();
					type_array = new Class[paramlen];
//					argv = new ArrayList<Object>();
					argv_array = new Object[paramlen];
					System.out.println("Inside paramatch");
					for (int i = 4, j = 0; j < paraArray.length; j++, i = i + 2) {
						if (paraArray[j].getName().equals(parts[i])) {
//							type.add(paraArray[j]);
							type_array[index] = paraArray[j];
//							argv.add(parts[i + 1]);
							argv_array[index] = conversion(parts[i + 1], parts[i]);
							index++;
							flag = 0;
						} else {
							flag = 1;
							break;
						}
					}
				} else {
					flag = 1;
				}
			} else {
				flag = 1;
			}
			if (flag == 0) {
				break;
			}
		}
		System.out.println("program run" + flag);
		if (flag == 1) {
			replystr = "Error, Given Method Signature Not Found on Server Side !!!";
		} else {
			Class c = null;
			try {
				System.out.println(className);
				className = "rpc_pck.InterfaceImplementation";
				c = Class.forName(className);
				System.out.println("class");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
//				 Object[] classArray = type.toArray();
				Method methodcall1 = c.getDeclaredMethod(methodName, type_array);

				Object ob = c.newInstance();
				System.out.println("object created");
//				Object[] arg = argv.toArray();
				System.out.println("toarray");
				for (Object i : argv_array) {
					System.out.print(i.toString());
				}
				System.out.println();

				Object r = methodcall1.invoke(ob, argv_array);

				System.out.println("Anwser : " + r.toString());
				replystr = r.toString();
				// methodcall1.invoke(obj, 19,20,14.6);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// sends output to the socket
		try {
			DataOutputStream out = null;
			out = new DataOutputStream(socket.getOutputStream());

			try {
				System.out.println("Sending to Client");
				out.writeUTF(replystr);
				System.out.println("sent");
			} catch (IOException i) {
				System.out.println(i);
			}

			// close connection
			socket.close();
			in.close();
		} catch (Exception e) {

		}

	}

}
