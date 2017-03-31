import java.awt.geom.Path2D;

public class CountyGeometry {
	public String name;
	public Path2D geom;
	public String wkt;
	
	public CountyGeometry(String n, Path2D g, String w){
		name = n;
		geom = g;
		wkt = w;
	}
}
