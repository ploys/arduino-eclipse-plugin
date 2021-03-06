package io.sloeber.core;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.sloeber.core.api.BoardDescriptor;
import io.sloeber.core.api.BoardsManager;
import io.sloeber.core.api.CodeDescriptor;
import io.sloeber.core.api.CompileOptions;
import io.sloeber.core.api.ConfigurationDescriptor;
import io.sloeber.core.boards.GenericJantjeBoard;
import io.sloeber.core.boards.IBoard;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class CreateAndCompileJantjesBoardsTest {
	private static int mCounter = 0;
	private CodeDescriptor myCodeDescriptor;
	private String myName;
	private boolean myUsesSerial1;
	private boolean myUsesKeyboard;
	private boolean myUsesSerial;
	private static int totalFails = 0;

	public CreateAndCompileJantjesBoardsTest(String name, CodeDescriptor codeDescriptor,
			boolean usesSerial, boolean usesSerial1, boolean usesKeyboard) {

		this.myCodeDescriptor = codeDescriptor;
		this.myName = name;
		this.myUsesSerial = usesSerial;
		this.myUsesSerial1 = usesSerial1;
		this.myUsesKeyboard = usesKeyboard;

	}

	@SuppressWarnings("rawtypes")
	@Parameters(name = "{index}: {0}")
	public static Collection examples() {
		LinkedList<Object[]> examples = new LinkedList<>();
		Shared.waitForAllJobsToFinish();
		BoardsManager.installLatestPlatform(GenericJantjeBoard.getJsonFileName(), GenericJantjeBoard.getPackageName(), GenericJantjeBoard.getPlatformName());


		TreeMap<String, IPath> exampleFolders = BoardsManager.getAllArduinoIDEExamples();
		for (Map.Entry<String, IPath> curexample : exampleFolders.entrySet()) {
			if(!skipExample(curexample.getKey())) {
			ArrayList<Path> paths = new ArrayList<>();

			paths.add(new Path(curexample.getValue().toString()));
			CodeDescriptor codeDescriptor = CodeDescriptor.createExample(false, paths);
			String inoName = curexample.getKey().trim();
			Boolean usesSerial = new Boolean(Examples.getUsesSerialExamples().contains(inoName));
			Boolean usesSerial1 = new Boolean(Examples.getUsesSerial1Examples().contains(inoName));
			Boolean usesKeyboard = new Boolean(Examples.getUsesKeyboardExamples().contains(inoName));

			Object[] theData = new Object[] { "Example:" + inoName, codeDescriptor, usesSerial, usesSerial1,
					usesKeyboard };
			examples.add(theData);
			}
		}

		return examples;

	}

	private static boolean skipExample(String curexample) {
		String searchName=curexample.trim().replace('?', '_');
		searchName=searchName.replace(' ', '_');
		searchName=searchName.replace('.', '_');
		switch (searchName) {
		case "examples__10_StarterKit_BasicKit_p13_TouchSensorLamp":
			return true;
		case "examples__09_USB_KeyboardAndMouseControl":
			return true;
		case "examples__09_USB__Mouse_JoystickMouseControl":
			return true;
		case "examples__09_USB__Mouse_ButtonMouseControl":
			return true;
		case "examples__09_USB__Keyboard_KeyboardSerial":
			return true;
		case "examples__09_USB__Keyboard_KeyboardReprogram":
			return true;
		case "examples__09_USB__Keyboard_KeyboardMessage":
			return true;
		case "examples__09_USB__Keyboard_KeyboardLogout":
			return true;
		}
		return false;
	}

	public void testExample(IBoard board) {
		// Stop after X fails because
		// the fails stays open in eclipse and it becomes really slow
		// There are only a number of issues you can handle
		// best is to focus on the first ones and then rerun starting with the
		// failures
		if (this.myUsesSerial && !board.supportsSerial()) {
			System.out.println("!TEST SKIPPED due to Serial " + this.myName + " " + board.getName());
			return;
		}
		if (this.myUsesSerial1 && !board.supportsSerial1()) {
			System.out.println("!TEST SKIPPED due to Serial1 " + this.myName + " " + board.getName());
			return;
		}
		if (this.myUsesKeyboard && !board.supportsKeyboard()) {
			System.out.println("!TEST SKIPPED due to keyboard " + this.myName + " " + board.getName());
			return;
		}
		if (totalFails < 40) {
			BuildAndVerify(board.getBoardDescriptor());
		} else {
			fail("To many fails. Stopping test");
		}

	}

	@Test
	public void testJantjeYun() {
		testExample(new GenericJantjeBoard("yun"));
	}

	@Test
	public void testJantjeUno() {
		testExample(new GenericJantjeBoard("uno"));
	}

	@Test
	public void testJantjeDiecimila() {
		testExample(new GenericJantjeBoard("diecimila"));
	}

	@Test
	public void testJantjeNano() {
		testExample(new GenericJantjeBoard("nano"));
	}

	@Test
	public void testJantjeMega() {
		testExample(new GenericJantjeBoard("mega"));
	}
	@Test
	public void testJantjeMegaADK() {
		testExample(new GenericJantjeBoard("megaADK"));
	}
	@Test
	public void testJantjeLeonardo() {
		testExample(new GenericJantjeBoard("leonardo"));
	}
	@Test
	public void testJantjeMicro() {
		testExample(new GenericJantjeBoard("micro"));
	}
	@Test
	public void testJantjeEsplora() {
		testExample(new GenericJantjeBoard("esplora"));
	}
	@Test
	public void testJantjeMini() {
		testExample(new GenericJantjeBoard("mini"));
	}
	@Test
	public void testJantjeEthernet() {
		testExample(new GenericJantjeBoard("ethernet"));
	}
	@Test
	public void testJantje_fio() {
		testExample(new GenericJantjeBoard("fio"));
	}
	@Test
	public void testJantje_bt() {
		testExample(new GenericJantjeBoard("bt"));
	}
	@Test
	public void testJantje_LilyPadUSB() {
		testExample(new GenericJantjeBoard("LilyPadUSB"));
	}
	@Test
	public void testJantje_lilypad() {
		testExample(new GenericJantjeBoard("lilypad"));
	}
	@Test
	public void testJantje_pro() {
		testExample(new GenericJantjeBoard("pro"));
	}
	@Test
	public void testJantje_atmegang() {
		testExample(new GenericJantjeBoard("atmegang"));
	}
	@Test
	public void testJantje_robotControl() {
		testExample(new GenericJantjeBoard("robotControl"));
	}



	public void BuildAndVerify(BoardDescriptor boardDescriptor) {

		IProject theTestProject = null;

		NullProgressMonitor monitor = new NullProgressMonitor();
		String projectName = String.format("%05d_:%s_%s", new Integer(mCounter++), this.myName,
				boardDescriptor.getBoardID());
		try {

			theTestProject = boardDescriptor.createProject(projectName, null,
					ConfigurationDescriptor.getDefaultDescriptors(), this.myCodeDescriptor, new CompileOptions(null),
					monitor);
			Shared.waitForAllJobsToFinish(); // for the indexer
		} catch (Exception e) {
			e.printStackTrace();
			totalFails++;
			fail("Failed to create the project:" + projectName);
			return;
		}
		try {
			theTestProject.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
			if (Shared.hasBuildErrors(theTestProject)) {
				// try again because the libraries may not yet been added
				Shared.waitForAllJobsToFinish(); // for the indexer
				try {
					Thread.sleep(3000);// seen sometimes the libs were still not
										// added
				} catch (InterruptedException e) {
					// ignore
				}
				theTestProject.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
				if (Shared.hasBuildErrors(theTestProject)) {
					// give up
					totalFails++;
					fail("Failed to compile the project:" + projectName + " build errors");
				} else {
					theTestProject.delete(true, null);
				}
			} else {
				theTestProject.delete(true, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			totalFails++;
			fail("Failed to compile the project:" + projectName + " exception");
		}
	}

}
