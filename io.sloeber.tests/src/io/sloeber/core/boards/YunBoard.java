package io.sloeber.core.boards;

@SuppressWarnings("nls")
public class YunBoard extends GenericArduinoAvrBoard {
	public YunBoard() {
		super("yun");
		this.myBoardDescriptor.setUploadPort("COM24");
	}

}
