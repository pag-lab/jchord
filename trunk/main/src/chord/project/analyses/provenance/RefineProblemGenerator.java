package chord.project.analyses.provenance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import chord.project.ClassicProject;
import chord.project.Config;
import chord.project.ITask;
import chord.project.analyses.JavaAnalysis;
import chord.project.analyses.ProgramRel;

public abstract class RefineProblemGenerator extends JavaAnalysis {
	public static String out = "tuples.txt";
	
	@Override
	public void run(){
		out = System.getProperty("chord.provenance.refineProblemOut", out);
		for(ITask task : this.getTasks()){
//			ClassicProject.g().resetTaskDone(task);
			ClassicProject.g().runTask(task);
		}
		try {
			PrintWriter pw = new PrintWriter(new File(Config.outDirName + File.separator + out));
			int sumInputWeight = 0;
			pw.println("Input tuples: ");
			for(String inputRelName : this.getInputRelations()){
				ProgramRel inRel = (ProgramRel) ClassicProject.g().getTrgt(inputRelName);
				inRel.load();
				for(int[] indices : inRel.getAryNIntTuples()){
					Tuple t = new Tuple(inRel,indices);
					int tw = this.getWeight(t);
					if(tw >= 0)
						sumInputWeight += tw;
					pw.println(t+" "+tw);
				}
			}
			
			pw.println("query tuples: ");
			
			ProgramRel qRel = (ProgramRel) ClassicProject.g().getTrgt(this.getQueryRelation());
			qRel.load();
			for(int[] indices : qRel.getAryNIntTuples()){
				Tuple t = new Tuple(qRel,indices);
				pw.println(t+" "+sumInputWeight);
			}
			
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * return the set of input relations.
	 * @return
	 */
	public abstract Set<String> getInputRelations();
	
	public abstract String getQueryRelation();

	/**
	 * return the weight of input tuples.
	 * -1 means this parameter is not a configurable tuple.
	 * @param t
	 * @return
	 */
	public abstract int getWeight(Tuple t);
	
	public abstract List<ITask> getTasks();
}
