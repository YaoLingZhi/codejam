import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Exec2013R1CCL {
	private static String filePath = "2013R1CC-large-practice.in"; // "2013R1CC-small-practice.in";  // "2013R1CC-TheGreatWall.in";
	public static final int  imax = 2147483647;
	public static final long lmax = 9223372036854775807l;

	private static int T = 0;  // 1 d" T d" 100
	private static int[] Ns  = null; // [T] 0 d" N d" 1000
	private static List<List<Attack>> attacks = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startDatetime = System.currentTimeMillis();
		try {
			input();

			StringBuffer answer = new StringBuffer();
			for (int x = 0; x < T; x++) {
//				if (x != 6) continue;
				answer.append("Case #" + (x+1) + ": ");
				System.out.print("Case #" + (x+1) + ": ");

				//int N = Ns[x];
				List<Attack> attack = attacks.get(x);
				int y = 0;

				List<Integer> wallP = new ArrayList<Integer>();
				List<Integer> wallH = new ArrayList<Integer>();
				wallP.add(attack.get(0).wi);
				wallH.add(0);
				wallP.add(attack.get(0).ei);
				wallH.add(0);
				Attack currAtt = null;
				while (attack.size() > 0) {
					int minDi = attack.get(0).di;
					List<Attack> currAtts = new ArrayList<Attack>();
					currAtts.add(attack.get(0));
					for (int i = 1; i < attack.size(); i++) {
						currAtt = attack.get(i);
						if (currAtt.di == minDi) currAtts.add(attack.get(i));
						else if (currAtt.di < minDi) {
							minDi = currAtt.di;
							currAtts.clear();
							currAtts.add(attack.get(i));
						}
					}
					
//					System.out.println("attack.size()= " + attack.size());
//					for (int i = 0; i < currAtts.size(); i++) System.out.println("[" + attack.indexOf(currAtts.get(i)) + "]: " + currAtts.get(i));
					
					// count the attack
					for (int i = 0; i < currAtts.size(); i++) {
						currAtt = currAtts.get(i);
						int wi = currAtt.wi;
						int ei = currAtt.ei;
						int si = currAtt.si;
						int st = 0, ed = wallP.size() - 1;
						if (wi < wallP.get(0) || ei > wallP.get(ed)) {
							y++;
							continue;
						}
						
						int wiP = 0, eiP = ed;
						if (wi == wallP.get(0)) wiP = 0;
						else {
							while (true) {
								wiP = (st + ed) / 2;
								if (wi < wallP.get(wiP)) {
									if (st + 1 == wiP) {
										wiP = st;
										break;
									}
									ed = wiP;
								} else if (wi == wallP.get(wiP)) {
									break;
								} else {
									if (wiP + 1 == ed) break;
									st = wiP;
								}
							}
						}
						ed = wallP.size() - 1;
						if (ei == wallP.get(ed)) eiP = ed - 1;
						else {
							st = wiP;
							while (true) {
								eiP = (st + ed) / 2;
								if (ei < wallP.get(eiP)) {
									if (st + 1 == eiP) {
										eiP = st;
										break;
									}
									ed = eiP;
								} else if (ei == wallP.get(eiP)) {
									eiP--;
									break;
								} else {
									if (eiP + 1 == ed) break;
									st = eiP;
								}
							}
						}
						for (int k = wiP; k <= eiP; k++) {
							if (wallH.get(k) < si) {
								y++;
								break;
							}
						}
					}
					
					// update the wall
					for (int i = 0; i < currAtts.size(); i++) {
						currAtt = currAtts.get(i);
						int wi = currAtt.wi;
						int ei = currAtt.ei;
						int si = currAtt.si;
						int st = 0, ed = wallP.size() - 1;
						if (wi < wallP.get(0)) {
							wallP.add(0, wi);
							wallH.add(0, si);
							if (ei < wallP.get(1)) {
								wallP.add(1, ei);
								wallH.add(1, 0);
								continue;
							} else if (ei == wallP.get(1)) continue;
							wi = wallP.get(1);
							st = 1;
						} else if (wi == wallP.get(ed)) {
							wallH.set(ed, si);
							wallP.add(ei);
							wallH.add(0);
							continue;
						} else if (wi > wallP.get(ed)) {
							wallP.add(wi);
							wallH.add(si);
							wallP.add(ei);
							wallH.add(0);
							continue;
						}
						
						int wiP = 0, eiP = 0;
						if (wi == wallP.get(st)) wiP = st;
						else {
							ed = wallP.size() - 1;
							while (true) {
								wiP = (st + ed) / 2;
								if (wi < wallP.get(wiP)) {
									if (st + 1 == wiP) {
										if (wallH.get(st) < si) {
											if (ei < wallP.get(wiP)) {
												wallP.add(wiP, wi);
												wallH.add(wiP, si);
												wiP++;
												wallP.add(wiP, ei);
												wallH.add(wiP, wallH.get(st));
												eiP = -1;
											} else {
												wallP.add(wiP, wi);
												wallH.add(wiP, si);
												wiP++;
											}
										}
										break;
									}
									ed = wiP;
								} else if (wi == wallP.get(wiP)) {
									break;
								} else {
									if (wiP + 1 == ed) {
										if (wallH.get(wiP) < si) {
											if (ei < wallP.get(ed)) {
												wallP.add(ed, wi);
												wallH.add(ed, si);
												wallP.add(ed+1, ei);
												wallH.add(ed+1, wallH.get(wiP));
												eiP = -1;
											} else {
												wallP.add(ed, wi);
												wallH.add(ed, si);
												wiP = ed + 1;
											}
										} else wiP = ed;
										break;
									}
									st = wiP;
								}
							}
						}
						if (eiP < 0 || ei <= wallP.get(wiP)) continue;
						ed = wallP.size() - 1;
						if (ei > wallP.get(ed)) {
							wallH.set(ed, si);
							wallP.add(ei);
							wallH.add(0);
							eiP = ed - 1;
						} else if (ei == wallP.get(ed)) eiP = ed - 1;
						else {
							st = wiP;
							while (true) {
								eiP = (st + ed) / 2;
								if (ei < wallP.get(eiP)) {
									if (st + 1 == eiP) {
										if (wallH.get(st) < si) {
											wallP.add(eiP, ei);
											wallH.add(eiP, wallH.get(st));
											wallH.set(st, si);
										}
										eiP = st - 1;
										break;
									}
									ed = eiP;
								} else if (ei == wallP.get(eiP)) {
									eiP--;
									break;
								} else {
									if (eiP + 1 == ed) {
										if (wallH.get(eiP) < si) {
											wallP.add(ed, ei);
											wallH.add(ed, wallH.get(eiP));
											wallH.set(eiP, si);
										}
										eiP--;
										break;
									}
									st = eiP;
								}
							}
						}
						
						boolean update = false;
						for (int k = wiP; k <= eiP; k++) {
							if (wallH.get(k) < si) {
								if (update) {
									wallP.remove(k);
									wallH.remove(k);
									k--;
									eiP--;
								} else {
									update = true;
									wallH.set(k, si);
								}
							} else update = false;
						}
					}
					
					for (int i = 0; i < currAtts.size(); i++) {
						currAtt = currAtts.get(i);
						if (currAtt.ni == 1) attack.remove(currAtt);
						else if (currAtt.delta_pi == 0 && currAtt.delta_si <= 0) attack.remove(currAtt);
						else {
							currAtt.ni--;
							currAtt.di += currAtt.delta_di;
							currAtt.wi += currAtt.delta_pi;
							currAtt.ei += currAtt.delta_pi;
							currAtt.si += currAtt.delta_si;
						}
					}
//					System.out.println("y=" + y);
//					for (int i = 0; i < wallP.size(); i++) System.out.print(wallP.get(i) + "\t");
//					System.out.println();
//					for (int i = 0; i < wallH.size(); i++) System.out.print(wallH.get(i) + "\t");
//					System.out.println("\r\nAfter:");
//					for (int i = 0; i < currAtts.size(); i++) System.out.println(currAtts.get(i));
//					System.out.println();
				}

				answer.append(y).append("\n");
				System.out.println(y);
			}
			output(answer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long tm = System.currentTimeMillis() - startDatetime;
		long tms = tm / 1000;
		System.out.println("The process time: " + tms + "s " + (tm%1000) + "ms");
	}

	/**
	 * read data from a file
	 * @param path : the data file's path
	 */
	private static void input() throws Exception {
		BufferedReader br = null;
		String line = null;
		if (filePath == null) br = new BufferedReader(new InputStreamReader(System.in));
		else br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

		T = Integer.parseInt(br.readLine());

		Ns = new int[T];
		attacks = new ArrayList<List<Attack>>();
		for (int x = 0; x < T; x++) {
			if ( (line = br.readLine()) == null) { throw new Exception("wrong! Case #" + (x+1)); }
			Ns[x] = Integer.parseInt(line);

			List<Attack> attack = new ArrayList<Attack>();
			for (int i = 0; i < Ns[x]; i++) {
				if ( (line = br.readLine()) == null) { throw new Exception("wrong! Case #" + (x+1)); }
				String[] sts = line.split(" ");
				attack.add(new Attack(sts));
			}
			attacks.add(attack);
		}
		br.close();
		br = null;

		// Confirm
//		System.out.println(T);
//		for (int x = 0; x < T; x++) {
//			System.out.println(Ns[x]);
//			for (int i = 0; i < Ns[x]; i++) {
//				System.out.println(attacks.get(x)[i]);
//			}
//		}
	}

	private static void output(StringBuffer answer) throws Exception {
		String outPath = "answer.out";
		if (filePath != null) outPath = filePath.substring(0, filePath.length()-2) + "out";
		FileOutputStream fos = new FileOutputStream(outPath, false);
		fos.write(answer.toString().getBytes());
		fos.close();
		fos = null;
	}
}

class Attack {
	public int di;
	public int ni;
	public int wi;
	public int ei;
	public int si;
	public int delta_di;
	public int delta_pi;
	public int delta_si;
	public int[] wia;
	public int[] eia;

	public Attack(String[] sts) {
		this.di = Integer.parseInt(sts[0]);
		this.ni = Integer.parseInt(sts[1]);
		this.wi = Integer.parseInt(sts[2]);
		this.ei = Integer.parseInt(sts[3]);
		this.si = Integer.parseInt(sts[4]);
		this.delta_di = Integer.parseInt(sts[5]);
		this.delta_pi = Integer.parseInt(sts[6]);
		this.delta_si = Integer.parseInt(sts[7]);
	}

	public String toString() {
		return di + " " + ni + " " + wi + " " + ei + " " + si + " " + delta_di + " " + delta_pi + " " + delta_si;
	}
}
