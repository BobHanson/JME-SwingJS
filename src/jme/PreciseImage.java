package jme;



import java.awt.Image;

public class PreciseImage {
	Image implImage;
	PreciseGraphicsAWT preciseGraphics;
	
	public Image getImage() {
		return implImage;
	}

	public PreciseImage(Image src) {
		this.implImage = src;
		this.preciseGraphics = new PreciseGraphicsAWT(src.getGraphics());
	}

	public PreciseGraphicsAWT getGraphics() {
		//this is not a nice implementation
		//it assumes that before drawing this method is called 
		//it can not be called a second time before the image has been rendered
		//works fine for JSME - potential bug
		return this.getGraphics(1.0);
	}

	public PreciseGraphicsAWT getGraphics(double scale) {
		//this is not a nice implementation
		//it assumes that before drawing this method is called 
		//it can not be called a second time before the image has been rendered
		//works fine for JSME - potential bug
		this.preciseGraphics.initPrecisionScale(scale); //must be called before any drawing is performed
		return this.preciseGraphics;
	}
}
