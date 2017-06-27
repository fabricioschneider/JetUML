package ca.mcgill.cs.stg.jetuml.diagrams;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ca.mcgill.cs.stg.jetuml.graph.ChildNode;
import ca.mcgill.cs.stg.jetuml.graph.DependencyEdge;
import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.Node;
import ca.mcgill.cs.stg.jetuml.graph.PackageDependencyEdge;
import ca.mcgill.cs.stg.jetuml.graph.PackageNode;


/**
 *  A UML class diagram.
 */
public class PackageDiagramGraph extends Graph
{
	private static final Node[] NODE_PROTOTYPES = new Node[] {new PackageNode()};
	
	private static final Edge[] EDGE_PROTOTYPES = new Edge[] {new DependencyEdge(),
			                                                  new PackageDependencyEdge(PackageDependencyEdge.Type.Merge), 
															  new PackageDependencyEdge(PackageDependencyEdge.Type.Import)};
															  
	@Override
	public Node[] getNodePrototypes()
	{
		return NODE_PROTOTYPES;
	}

	@Override
	public Edge[] getEdgePrototypes()
	{
		return EDGE_PROTOTYPES;
	}

	@Override
	public String getFileExtension() 
	{
		return ResourceBundle.getBundle("ca.mcgill.cs.stg.jetuml.UMLEditorStrings").getString("package.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("ca.mcgill.cs.stg.jetuml.UMLEditorStrings").getString("package.name");
	}

	private static boolean canAddNodeAsChild(Node pPotentialChild)
	{
		return pPotentialChild instanceof PackageNode ;
	}
	
	@Override
	public boolean canConnect(Edge pEdge, Node pNode1, Node pNode2, Point2D pPoint2)
	{
		if( !super.canConnect(pEdge, pNode1, pNode2, pPoint2) )
		{
			return false;
		}
		
		return true;
	}
	
	/* Find if the node to be added should be added to a package. Returns null if not. 
	 * If packages overlap, select the last one added, which by default should be on
	 * top. This could be fixed if we ever add a z coordinate to the graph.
	 */
	private PackageNode findContainer( List<Node> pNodes, Point2D pPoint)
	{
		PackageNode container = null;
		for( Node node : pNodes )
		{
			if( node instanceof PackageNode && node.contains(pPoint) )
			{
				container = (PackageNode) node;
			}
		}
		if( container == null )
		{
			return null;
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Node> children = new ArrayList(container.getChildren());
		if( children.size() == 0 )
		{
			return container;
		}
		else
		{
			PackageNode deeperContainer = findContainer( children, pPoint );
			if( deeperContainer == null )
			{
				return container;
			}
			else
			{
				return deeperContainer;
			}
		}
	}
	
	/* (non-Javadoc)
	 * See if, given its position, the node should be added as a child of
	 * a PackageNode.
	 * 
	 * @see ca.mcgill.cs.stg.jetuml.graph.Graph#addNode(ca.mcgill.cs.stg.jetuml.graph.Node, java.awt.geom.Point2D)
	 */
	@Override
	public boolean addNode(Node pNode, Point2D pPoint)
	{
		if( canAddNodeAsChild(pNode))
		{
			PackageNode container = findContainer(aRootNodes, pPoint);
			if( container != null )
			{
				container.addChild((ChildNode)pNode);
			}
		}
		super.addNode(pNode, pPoint);
		return true;
	}
}





