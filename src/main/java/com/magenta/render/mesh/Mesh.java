package com.magenta.render.mesh;

// Store VAO ID and the number of vertex
public class Mesh {
	private int vao, vertices;

	public Mesh(int vao, int vertex) {
		this.vao = vao;
		this.vertices = vertex;
	}

	public int getVaoID() {
	    return vao;
	}

	public int getVertexCount() {
	    return vertices;
	}
}