package main;
import java.util.*;

public class SateliteList extends ArrayList<BoxList> {
	
	public SateliteList() {
		super();
	}
	public void draw(){
		for (int i = 0; i < this.size(); i++) {
			this.get(i).draw();
		}
	}
	
}
