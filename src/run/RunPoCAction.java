package run;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;

import GUI.MainGUI;
import PoC.PoCPanel;

public class RunPoCAction{

	public PrintWriter stdout;
	public PrintWriter stderr;

	private List<String> targets;
	private String poc;
	public RunPoCAction(List<String> targets,String poc) {
		this.targets = targets;
		this.poc = poc;
	}

	public void run() {
		try{
			boolean useRobot = (PoCPanel.rdbtnUseRobotInput.isSelected());
			if (useRobot) {
				RobotInput.startCmdConsole();//尽早启动减少出错概率
			}
			
			String para = "";
			if (targets.size() <=0) {
				return;
			}else if (targets.size() ==1) {
				para = "-s "+poc.trim()+" -iS "+targets.get(0);
			}else {
				File tmpTargets = new File("tmpTargets.txt");
				FileUtils.writeByteArrayToFile(tmpTargets, String.join(System.lineSeparator(),targets).getBytes());
				para = "-s "+poc.trim()+" -iF "+tmpTargets.getAbsolutePath();
			}
			
			String poctPath = "PoC-T.py";
			if (new File(MainGUI.poctRootPath).exists()) {
				poctPath  = MainGUI.poctRootPath+File.separator+"PoC-T.py";
			}
			//POC-T.py -s cas-deser-RCE -iS 127.0.0.1
			
			RobotInput ri = new RobotInput();
			if (useRobot) {
				String command = RobotInput.genCmd(null,poctPath,para);
				ri.inputString(command);
			}else {
				TerminalExec exec = new TerminalExec(null,"poct-fiora.bat",null,poctPath,para);
				exec.run();
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public static void main(String[] args){
	}
}