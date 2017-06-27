package ca.mcgill.cs.stg.jetuml.graph;

import java.awt.geom.Point2D;

import ca.mcgill.cs.stg.jetuml.framework.ArrowHead;
import ca.mcgill.cs.stg.jetuml.framework.LineStyle;
import ca.mcgill.cs.stg.jetuml.framework.SegmentationStyleFactory;

/**
 *  An edge that that represents a UML dependency
 *  between package.
 */
public class PackageDependencyEdge extends SegmentedLabeledEdge
{
	private static final String LABEL_MERGE = "\u00ABmerge\u00BB";
	private static final String LABEL_IMPORT = "\u00ABimport\u00BB";
	
	/**
	 * Type of package dependency.
	 */
	public enum Type 
	{None, Merge, Import}
	
	private Type aType = Type.None;
	
	/**
	 * Creates a default (un-typed) dependency.
	 */
	public PackageDependencyEdge()
	{}
	
	/**
	 * Creates a typed dependency.
	 * @param pType The type of dependency.
	 */
	public PackageDependencyEdge(Type pType)
	{
		aType = pType;
	}
	
	/**
	 * @return The type of dependency relation.
	 */
	public Type getType()
	{
		return aType;
	}
	
	/**
	 * @param pType The type of dependency relation.
	 */
	public void setType(Type pType)
	{
		aType = pType;
	}
	
	@Override
	protected ArrowHead obtainEndArrowHead()
	{
		return ArrowHead.V;
	}
	
	@Override
	protected LineStyle obtainLineStyle()
	{
		return LineStyle.DOTTED;
	}
	
	@Override
	protected String obtainMiddleLabel()
	{
		if( aType == Type.Merge )
		{
			return LABEL_MERGE;
		}
		else if( aType == Type.Import )
		{
			return LABEL_IMPORT;
		}
		else
		{
			return "";
		}
	}
	
	@Override
	protected Point2D[] getPoints()
	{
		return SegmentationStyleFactory.createStraightStrategy().getPath(this, getGraph());
	}
}
