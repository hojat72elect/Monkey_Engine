
package com.jme3.util.mikktspace;

/**
 *
 * @author Nehon
 */
public interface MikkTSpaceContext {

    /**
     * Returns the number of faces (triangles/quads) on the mesh to be
     * processed.
     *
     * @return the count (&ge;0)
     */
    public int getNumFaces();

    /**
     * Returns the number of vertices on face number iFace iFace is a number in
     * the range {0, 1, ..., getNumFaces()-1}
     *
     * @param face which face (&ge;0, &lt;numFaces)
     * @return the count (&ge;0)
     */
    public int getNumVerticesOfFace(int face);

    /**
     * returns the position/normal/texcoord of the referenced face of vertex
     * number iVert. iVert is in the range {0,1,2} for triangles and {0,1,2,3}
     * for quads.
     *
     * @param posOut storage for the results (modified)
     * @param face which face (&ge;0, &lt;numFaces)
     * @param vert which vertex in the face (&ge;0, &lt;numVertices)
     */
    public void getPosition(float posOut[], int face, int vert);

    public void getNormal(float normOut[], int face, int vert);

    public void getTexCoord(float texOut[], int face, int vert);

    /**
     * The callback setTSpaceBasic() is sufficient for basic normal mapping.
     * This function is used to return the tangent and sign to the application.
     * tangent is a unit length vector. For normal maps it is sufficient to use
     * the following simplified version of the bitangent which is generated at
     * pixel/vertex level.
     *
     * bitangent = fSign * cross(vN, tangent);
     *
     * Note that the results are returned unindexed. It is possible to generate
     * a new index list But averaging/overwriting tangent spaces by using an
     * already existing index list WILL produce INCORRECT results. DO NOT! use
     * an already existing index list.
     *
     * @param tangent the desired tangent vector (unaffected)
     * @param sign the desired sign
     * @param face which face (&ge;0, &lt;numFaces)
     * @param vert which vertex in the face (&ge;0, &lt;numVertices)
     */
    public void setTSpaceBasic(float tangent[], float sign, int face, int vert);

    /**
     * This function is used to return tangent space results to the application.
     * tangent and biTangent are unit length vectors and fMagS and fMagT are
     * their true magnitudes which can be used for relief mapping effects.
     *
     * biTangent is the "real" bitangent and thus may not be perpendicular to
     * tangent. However, both are perpendicular to the vertex normal. For normal
     * maps it is sufficient to use the following simplified version of the
     * bitangent which is generated at pixel/vertex level.
     *
     * <pre>
     * fSign = bIsOrientationPreserving ? 1.0f : (-1.0f);
     * bitangent = fSign * cross(vN, tangent);
     * </pre>
     *
     * Note that the results are returned unindexed. It is possible to generate
     * a new index list. But averaging/overwriting tangent spaces by using an
     * already existing index list WILL produce INCORRECT results. DO NOT! use
     * an already existing index list.
     *
     * @param tangent the desired tangent vector (unaffected)
     * @param biTangent the desired bitangent vector (unaffected)
     * @param magS true magnitude of S
     * @param magT true magnitude of T
     * @param isOrientationPreserving true&rarr;preserves, false&rarr;doesn't
     * preserve
     * @param face which face (&ge;0, &lt;numFaces)
     * @param vert which vertex in the face (&ge;0, &lt;numVertices)
     */
    void setTSpace(float tangent[], float biTangent[], float magS, float magT,
            boolean isOrientationPreserving, int face, int vert);
}
