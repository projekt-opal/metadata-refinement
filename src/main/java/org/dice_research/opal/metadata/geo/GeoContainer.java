package org.dice_research.opal.metadata.geo;

/**
 * Container for geo data (lower-case label, latitude and longitude).
 *
 * @author Adrian Wilke
 */
public class GeoContainer implements Comparable<GeoContainer> {

	/**
	 * Longer labels first to prefer them during extraction.
	 */
	public static int compare(String o1, String o2) {
		if (o1.length() > o2.length()) {
			return -1;
		} else if (o1.length() < o2.length()) {
			return 1;
		} else {
			return o1.compareTo(o2);
		}
	}

	public String label;
	public float lat;
	public float lon;
	public String uri;

	@Override
	public String toString() {
		return label + " " + lat + " " + lon;
	}

	public boolean hasLabelAndCoords() {
		return (label != null && !label.isEmpty() && lat != 0 && lon != 0);
	}

	@Override
	public int compareTo(GeoContainer o) {
		return compare(label, o.label);
	}
}