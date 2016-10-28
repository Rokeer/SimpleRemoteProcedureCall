import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StubGenerator {
	String filename = "specification.txt";
	ArrayList<ArrayList<String>> procedures = new ArrayList<ArrayList<String>>();
	String Prog_Name = "";
	String Prog_Version = "";

	public StubGenerator() {

	}

	public void stubGen() {
		readFile();
		genHeaderInterface();
		genClientStub();
		genClient();
		genServerStub();
		genServerThread();
		genServer();

	}

	public void genHeaderInterface() {
		String text = "";
		String file = "HeaderInterface";
		String templateFile = file + "_template.java";
		String methods = "";
		text = readTemplate(templateFile);
		for (int i = 0; i < procedures.size(); i++) {
			ArrayList<String> p = procedures.get(i);
			methods = methods + p.get(1) + " " + p.get(2) + "(";
			for (int j = 3; j < p.size(); j = j + 2) {
				methods = methods + p.get(j + 1) + " " + p.get(j) + ", ";
			}
			if (p.size() <= 3) {
				methods = methods + ", ";
			}
			methods = methods.substring(0, methods.length() - 2) + ");\n\n\t";
		}
		methods = methods.substring(0, methods.length() - 3);
		text = text.replace("$methods$", methods);
		System.out.println(text);
		writeTemplate(file + ".java", text);
	}

	public void genClientStub() {
		String text = "";
		String file = "ClientStub";
		String templateFile = file + "_template.java";
		text = readTemplate(templateFile);
		text = text.replace("$Prog_Name$", Prog_Name);
		text = text.replace("$Prog_Version$", Prog_Version);

		String[] blocks = text.split("\\$procedure_block\\$");
		text = blocks[0];

		for (int i = 0; i < procedures.size(); i++) {
			String pText = blocks[1];
			String procedure_name = "";
			String returnText = "";
			String procedure_paras = "";
			ArrayList<String> p = procedures.get(i);

			pText = pText.replace("$procedure_id$", p.get(0));

			procedure_name = procedure_name + p.get(1) + " " + p.get(2) + "(";
			for (int j = 3; j < p.size(); j = j + 2) {
				procedure_name = procedure_name + p.get(j + 1) + " " + p.get(j) + ", ";
				procedure_paras = procedure_paras + "mStrMSG = mStrMSG + ObjectUtil.toString(" + p.get(j)
						+ ") + \",\";\n\t\t\t";
			}
			if (p.size() <= 3) {
				procedure_name = procedure_name + ", ";
				procedure_paras = procedure_paras + ") + \",\";\n\t\t\t";
			}
			procedure_name = procedure_name.substring(0, procedure_name.length() - 2) + ")";
			procedure_paras = procedure_paras.substring(0, procedure_paras.length() - 4);
			pText = pText.replace("$procedure_name$", procedure_name);
			if (p.size() <= 3) {
				pText = pText.replace("$procedure_paras$", "");
			} else {
				pText = pText.replace("$procedure_paras$", procedure_paras);
			}
			

			if (!p.get(1).equals("void")) {
				returnText = "return (" + p.get(1) + ") result;";
				pText = pText.replace("$return$", returnText);
			} else {
				pText = pText.replace("$return$", "");
			}

			text = text + pText + "\n";

		}

		text = text.substring(0, text.length() - 1) + blocks[2];
		System.out.println(text);
		writeTemplate(file + ".java", text);
	}

	public void genClient() {
		String text = "";
		String file = "Client";
		String templateFile = file + "_template.java";
		text = readTemplate(templateFile);
		writeTemplate(file + ".java", text);
	}

	public void genServerStub() {
		String text = "";
		String file = "ServerStub";
		String templateFile = file + "_template.java";
		text = readTemplate(templateFile);
		text = text.replace("$Prog_Name$", Prog_Name);
		text = text.replace("$Prog_Version$", Prog_Version);
		System.out.println(text);
		writeTemplate(file + ".java", text);
	}

	public void genServerThread() {
		String text = "";
		String file = "ServerThread";
		String templateFile = file + "_template.java";
		String methods = "";
		text = readTemplate(templateFile);
		text = text.replace("$Prog_Name$", Prog_Name);
		text = text.replace("$Prog_Version$", Prog_Version);
		String cases = "";

		for (int i = 0; i < procedures.size(); i++) {
			ArrayList<String> p = procedures.get(i);
			String returnText = p.get(2) + "(";
			cases = cases + "case " + p.get(0) + ":\n\t\t\t\t\t\t\ttry {\n";
			cases = cases
					+ "\t\t\t\t\t\t\t\tSystem.out.println(\"Server: Procedure \" + procedure + \" is executing\");\n";
			methods = methods + "@Override\n\tpublic " + p.get(1) + " " + p.get(2) + "(";
			for (int j = 3; j < p.size(); j = j + 2) {
				methods = methods + p.get(j + 1) + " " + p.get(j) + ", ";
				returnText = returnText + p.get(j) + ", ";
				cases = cases + "\t\t\t\t\t\t\t\t" + p.get(j + 1) + " " + p.get(j) + " = (" + p.get(j + 1)
						+ ") ObjectUtil.fromString(msgs[" + ((j - 3) / 2 + 4) + "]);\n";
			}
			if (p.size() <= 3) {
				methods = methods + ", ";
				returnText = returnText + ", ";
				cases = cases + "\t\t\t\t\t\t\t\tresult = \"1,\" + msgs[0];\n";
			} else {
				cases = cases + "\t\t\t\t\t\t\t\tresult = result + ObjectUtil.toString(" + returnText.substring(0, returnText.length() - 2) + "));\n";
			}
			
			cases = cases + "\t\t\t\t\t\t\t} catch (Exception e) {\n";
			cases = cases + "\t\t\t\t\t\t\t\te.printStackTrace();\n";
			cases = cases + "\t\t\t\t\t\t\t}\n";

			
			cases = cases + "\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\t";
			methods = methods.substring(0, methods.length() - 2) + "){\n";
			returnText = "s." + returnText.substring(0, returnText.length() - 2) + ");";
			if (!p.get(1).equals("void")) {
				returnText = "return " + returnText;
			}
			returnText = "\t\t" + returnText + "\n";
			methods = methods + returnText;
			methods = methods + "\t}\n\n\t";
		}
		methods = methods.substring(0, methods.length() - 3);
		text = text.replace("$methods$", methods);
		text = text.replace("$cases$", cases.substring(0, cases.length()-7));
		System.out.println(text);
		writeTemplate(file + ".java", text);
	}

	public void genServer() {
		String text = "";
		String file = "Server";
		String templateFile = file + "_template.java";
		String methods = "";
		text = readTemplate(templateFile);
		for (int i = 0; i < procedures.size(); i++) {
			ArrayList<String> p = procedures.get(i);
			methods = methods + "@Override\n\tpublic " + p.get(1) + " " + p.get(2) + "(";
			for (int j = 3; j < p.size(); j = j + 2) {
				methods = methods + p.get(j + 1) + " " + p.get(j) + ", ";
			}
			if (p.size() <= 3) {
				methods = methods + ", ";
			}
			methods = methods.substring(0, methods.length() - 2) + "){\n\t\t\n\t}\n\n\t";
		}
		methods = methods.substring(0, methods.length() - 3);
		text = text.replace("$methods$", methods);
		System.out.println(text);
		writeTemplate(file + ".java", text);
	}

	public void readFile() {
		String line;
		String text = "";

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(filename);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				// line = line.trim();
				text = text + line.trim();
			}
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + filename + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		System.out.println(text);
		String[] blocks = text.split("\\{");
		blocks = blocks[2].trim().split("}");
		String[] tmpBlocks = blocks[1].split("=");
		Prog_Version = tmpBlocks[1].trim();
		tmpBlocks = blocks[2].split("=");
		Prog_Name = tmpBlocks[1].trim();

		System.out.println(Prog_Name);
		System.out.println(Prog_Version);

		tmpBlocks = blocks[0].split(";");
		for (int i = 0; i < tmpBlocks.length; i++) {
			ArrayList<String> procedure = new ArrayList<String>();

			String[] pBlocks = tmpBlocks[i].trim().split("=");
			String pId = pBlocks[1].trim();
			procedure.add(pId);
			System.out.println(pBlocks[0]);
			pBlocks[0] = pBlocks[0].replace("(", "");
			pBlocks[0] = pBlocks[0].replace(")", "");
			pBlocks[0] = pBlocks[0].replace(",", "");
			pBlocks = pBlocks[0].split(" ");
			// System.out.println(pId);
			for (int j = 0; j < pBlocks.length; j++) {
				procedure.add(pBlocks[j]);
				// System.out.println(pBlocks[j]);
			}
			procedures.add(procedure);
		}

	}

	public void writeTemplate(String file, String text) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file));
			out.write(text);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String readTemplate(String file) {
		String line;
		String text = "";

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(file);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				// line = line.trim();
				text = text + line + "\n";
			}
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + file + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + file + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return text.substring(0, text.length() - 1);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StubGenerator sg = new StubGenerator();
		sg.stubGen();
	}

}
