/*
 * Copyright (c) 2008-2009, Intel Corporation.
 * Copyright (c) 2006-2007, The Trustees of Stanford University.
 * All rights reserved.
 */
package chord.doms;

import chord.project.Program;
import chord.project.ProgramDom;
import chord.project.Project;

import joeq.Class.jq_Class;
import joeq.Class.jq_Method;
import joeq.Compiler.Quad.Quad;

public abstract class QuadDom extends ProgramDom<Quad> {
	protected DomM domM;
	protected jq_Method ctnrMethod;
	public void init() {
		domM = (DomM) Project.getTrgt("M");
	}
	public void visit(jq_Class c) { }
	public void visit(jq_Method m) {
		ctnrMethod = m;
	}
	public int getOrAdd(Quad q) {
		// XXX
		if (q != null)
			Program.v().mapInstToMethod(q, ctnrMethod);
		return super.getOrAdd(q);
	}
	public String toUniqueIdString(Quad q) {
		if (q == null)
			return "null";
		jq_Method m = Program.v().getMethod(q);
		int bci = m.getBCI(q);
		String mName = m.getName().toString();
		String mDesc = m.getDesc().toString();
		String cName = m.getDeclaringClass().getName();
		return Program.toString(bci, mName, mDesc, cName);
	}
	public String toXMLAttrsString(Quad q) {
		jq_Method m = Program.v().getMethod(q);
		String file = Program.getSourceFileName(m.getDeclaringClass());
		int line = Program.getLineNumber(q, m);
		int mIdx = domM.indexOf(m);
		return "file=\"" + file + "\" " + "line=\"" + line + "\" " +
			"Mid=\"M" + mIdx + "\"";
	}
	public String toString(Quad q) {
		return Program.v().toString(q);
	}
}
