/*
 * Copyright (c) 2008-2010, Intel Corporation.
 * Copyright (c) 2006-2007, The Trustees of Stanford University.
 * All rights reserved.
 */
package chord.project.analyses.rhs;

/**
 * Implementation of the Reps-Horwitz-Sagiv algorithm for
 * summary-based backward dataflow analysis.
 * 
 * @author Mayur Naik (mhn@cs.stanford.edu)
 */
public abstract class BackwardRHSAnalysis<PE extends IEdge, SE extends IEdge>
		extends RHSAnalysis<PE, SE> {
	@Override
	public boolean isForward() { return false; }
}
