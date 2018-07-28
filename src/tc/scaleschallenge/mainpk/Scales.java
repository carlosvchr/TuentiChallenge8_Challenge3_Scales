package tc.scaleschallenge.mainpk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;

public class Scales {

	/* Major and minor scales displacements (2 is for tones and 1 for semitones)
	 * This is because We have divided the notes in semitones, and to go to the 
	 * next tone, we have to advance 2 semitones. */
	private static final int majorScale[] = {2, 2, 1, 2, 2, 2, 1};
	private static final int minorScale[] = {2, 1, 2, 2, 1, 2, 2};
	// We use Natural Tonics when it is possible, # is used in other case
	private static final String pianoNotes[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	// We use this hashtable to find the pianoNote quicker
	private static Hashtable<String, Integer> pianoFinder;
	private static Hashtable<String, String> normalizer;
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);	
		// Gets the input file path from the user.
		System.out.print("Input file path: ");
		String ipath = s.nextLine();
		
		// Gets the input file path from the user.
		System.out.print("Output file path (e.g. /home/user/output.txt): ");		
		String opath = s.nextLine();
		
		// Closes the scanner stream
		s.close();
		
		// Gets the reader and writer
		BufferedReader br = IOManager.getReader(ipath);
		PrintWriter pw = IOManager.getWriter(opath);

		// Check writer and reader has been created successfully. Else, ends the program safely.
		if(br == null || pw == null) {
			System.out.println("Error when reader or writer has been created.");
			return;
		}
		
		pianoFinder = new Hashtable<>();
		normalizer = new Hashtable<>();
		
		// Initialize hashtable
		for(int i=0; i<pianoNotes.length; i++) {
			pianoFinder.put(pianoNotes[i], i);
		}
		
		// Initialize normalizer hashtable
		initNormalizer();
		
		// Calculates all cases and generate the output
		if(processCases(br, pw)) {
			System.out.println("Results has been generated successfully!");
		}else {
			System.out.println("Terminated with errors.");
		}
		
		// Closes input and output streams
		IOManager.closeStreams(br, pw);

	}
	
	/** Process all cases and write the output */
	public static boolean processCases(BufferedReader br, PrintWriter pw) {
		try {
			int ncases = Integer.parseInt(br.readLine());
			for(int i=0; i<ncases; i++) {
				String usedNotes[];
				// We check if it has at least 1 note
				int nnotes = Integer.parseInt(br.readLine());
				if(nnotes != 0) {
					// Store different notes used in the piece without duplicates
					usedNotes = new HashSet<String>(Arrays.asList(br.readLine().split(" "))).toArray(new String[0]);
				}else {
					usedNotes = new String[0];
				}
				
				// Normalize the notes, e.g. Ab -> G#
				usedNotes = normalize(usedNotes);
				
				// Keys
				ArrayList<String> keys = new ArrayList<>();
				// Iterate all piano notes
				for(int j=0; j<pianoNotes.length; j++) {
					// Get the major scale for given piano note
					HashSet<String> scaleNotes = getScale(pianoNotes[j], majorScale);
					if(isValid(scaleNotes, usedNotes)) {
						keys.add("M"+pianoNotes[j]);
					}
					// Get the minor scale for given piano note
					scaleNotes = getScale(pianoNotes[j], minorScale);
					if(isValid(scaleNotes, usedNotes)) {
						keys.add("m"+pianoNotes[j]);
					}
				}
				// Sort the keys alphabetically
				String result[] = keys.toArray(new String[keys.size()]);
				Arrays.sort(result);
				
				// Print output in asked format
				String output = "Case #"+(i+1)+":";
				if(result.length > 0) {
					for(String s : result) {
						output += " "+s;
					}
				}else {
					output += " None";
				}
				pw.println(output);
			}
		}catch(NumberFormatException nfe) {
			// Error when parsing number of cases
			System.out.println("File hasn't got a right format.");
			nfe.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/** Check all notes belong to the scale */
	public static boolean isValid(HashSet<String> hs, String notes[]) {
		if(notes.length == 0) return true;
		boolean allContained = true;		
		for(String s : notes) {
			if(!hs.contains(s)) {
				allContained = false;
			}
		}	
		return allContained;
	}
	
	/** Gets the notes associated to a defined scale */
	public static HashSet<String> getScale(String key, int scale[]) {	
		int index = pianoFinder.get(key);
		//String notes[] = new String[scale.length];
		HashSet<String> notes = new HashSet<>();
		for(int i=0; i<scale.length; i++) {
			notes.add(pianoNotes[index%pianoNotes.length]);
			index+=scale[i];
		}	
		return notes;
	}
	
	
	/** Normalize all equivalent notes to a standard */
	public static String[] normalize(String notes[]) {
		String res[] = new String[notes.length];
		for(int i=0; i<notes.length; i++) {
			res[i] = normalizer.get(notes[i]);
		}	
		return res;
	}
	
	/** Initialize normalizer hashtable to normalize all equivalent notes */
	public static void initNormalizer() {
		normalizer.put("A", "A");
		normalizer.put("B", "B");
		normalizer.put("C", "C");
		normalizer.put("D", "D");
		normalizer.put("E", "E");
		normalizer.put("F", "F");
		normalizer.put("G", "G");
		normalizer.put("A#", "A#");
		normalizer.put("B#", "C");
		normalizer.put("C#", "C#");
		normalizer.put("D#", "D#");
		normalizer.put("E#", "F");
		normalizer.put("F#", "F#");
		normalizer.put("G#", "G#");
		normalizer.put("Ab", "G#");
		normalizer.put("Bb", "A#");
		normalizer.put("Cb", "B");
		normalizer.put("Db", "C#");
		normalizer.put("Eb", "D#");
		normalizer.put("Fb", "E");
		normalizer.put("Gb", "F#");
	}

}
