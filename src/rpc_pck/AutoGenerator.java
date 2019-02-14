package rpc_pck;




import java.awt.image.ConvolveOp;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;


public class AutoGenerator {
	
	public static String code = "	public static Object doCommunication(String sentString, String addr, int p) {\n" + 
			"		String ans = null;\n" + 
			"		Socket socket = null;\n" + 
			"		DataInputStream readFromServer = null;\n" + 
			"		DataOutputStream out = null;\n" + 
			"		String address = addr;\n" + 
			"		int port = p;\n" + 
			"		String[] parts = sentString.split(\"@\");\n" + 
			"		String returnType = parts[1];\n" + 
			"		// establish a connection\n" + 
			"		try {\n" + 
			"			socket = new Socket(address, port);\n" + 
			"\n" + 
			"			// takes input from terminal\n" + 
			"			readFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));\n" + 
			"\n" + 
			"			// sends output to the socket\n" + 
			"			out = new DataOutputStream(socket.getOutputStream());\n" + 
			"		} catch (UnknownHostException u) {\n" + 
			"			System.out.println(u);\n" + 
			"		} catch (IOException i) {\n" + 
			"			System.out.println(i);\n" + 
			"		}\n" + 
			"\n" + 
			"		try {\n" + 
			"			out.writeUTF(sentString);\n" + 
			"		} catch (IOException i) {\n" + 
			"			System.out.println(i);\n" + 
			"		}\n" + 
			"		try {\n" + 
			"			ans = readFromServer.readUTF();\n" + 
			"\n" + 
			"		} catch (IOException i) {\n" + 
			"			System.out.println(i);\n" + 
			"		}\n" + 
			"\n" + 
			"		// close the connection\n" + 
			"		try {\n" + 
			"			readFromServer.close();\n" + 
			"			out.close();\n" + 
			"			socket.close();\n" + 
			"		} catch (IOException i) {\n" + 
			"			System.out.println(i);\n" + 
			"		}\n" + 
			"		return conversion(ans, returnType);\n" + 
			"	}\n";
	
	public static String conversionFun = "public static Object conversion(String value, String type) {\n" + 
			"		if (type.equals(\"int\")) {\n" + 
			"			return Integer.parseInt(value);\n" + 
			"		} else if (type.equals(\"String\")) {\n" + 
			"			return value;\n" + 
			"		} else if (type.equals(\"double\")) {\n" + 
			"			return Double.parseDouble(value);\n" + 
			"		} else if (type.equals(\"float\")) {\n" + 
			"			return Float.parseFloat(value);\n" + 
			"		} else if (type.equals(\"char\")) {\n" + 
			"			return value.charAt(0);\n" + 
			"		} else if (type.equals(\"boolean\")) {\n" + 
			"			return Boolean.parseBoolean(value);\n" + 
			"		} else {\n" + 
			"			return value;\n" + 
			"		}\n" + 
			"	}\n";

	public static void generateClass_Code() {
		String fullIName = CodeInterface.class.getCanonicalName();
		String packName = fullIName.substring(0,fullIName.indexOf('.'));
		String className = fullIName.substring(fullIName.indexOf('.') + 1)+"_";
		System.out.println("Class Name:- "+className);
		File fl = new File("src/"+packName+"/"+className+".java");
		boolean flag = false;
		try {
			flag = fl.createNewFile();
		} catch (IOException ioe) {
			System.out.println("Error in creating Java file" + ioe);
		}
		String ans="";
		ans += "package "+packName+";\n" ;
		ans += "import java.io.BufferedInputStream;\n" + 
				"import java.io.DataInputStream;\n" + 
				"import java.io.DataOutputStream;\n" + 
				"import java.io.File;\n" + 
				"import java.io.FileWriter;\n" + 
				"import java.io.IOException;\n" + 
				"import java.lang.reflect.Method;\n" + 
				"import java.net.Socket;\n" + 
				"import java.net.UnknownHostException;\n\n";
		
		ans += "public class "+className+ " { \n";
		
		
		Method[] classMethods = CodeInterface.class.getDeclaredMethods();
		for (Method md : classMethods) {
			ans += md.getReturnType() + " " + md.getName() + " (";
			Class[] paraArray = md.getParameterTypes();
			int i=1;
			String msgsent = className + "@" + md.getReturnType()+ "@" +md.getName() + "@" +paraArray.length;
			for(Class argv : paraArray)
			{
				String arvgs = argv.getName();
				ans += arvgs +" " + "a"+i + ", "; 
				msgsent += "@"+argv.getName() + "@\"+" +"a"+i+"+\"";	
				i++;
			}
			msgsent = msgsent.substring(0,msgsent.length()-2);
//			System.out.println(msgsent);
			ans = ans.substring(0,ans.length()-2);
			ans += ")\n";
			
					
			ans += "{" + "\n" +"return ("+ md.getReturnType() +")doCommunication("+"\""+msgsent+" ,\"127.0.0.1\", 9999);" +"}\n";
		}
		ans += code;
		ans += conversionFun;
		ans += "}";

		 try{    
	           FileWriter fw=new FileWriter(fl); 
	           fw.write(ans);    
	           fw.close();    
	     }
		 catch(Exception e)
		 {
			 System.out.println("Error into writting into File");
		 }
	       	
	}
}
