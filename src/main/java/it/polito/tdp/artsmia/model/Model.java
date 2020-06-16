package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private List<String> ruoli;
	private ArtsmiaDAO dao = new ArtsmiaDAO();
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Map<Integer,String> mappaArtisti;
	
	public Model() {
		this.mappaArtisti = new HashMap<Integer,String>();
	}
	
	public List<String> getRuoli(){
		this.ruoli = new LinkedList<String>(this.dao.getRole());
		return ruoli;
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getArtisti(ruolo));
		System.out.println("Size: "+ this.dao.getArtisti(ruolo).size());
		
		
		for(Arco a : this.dao.getArchi()) {
			if(this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
				DefaultWeightedEdge edge = this.grafo.getEdge(a.getA1(), a.getA2());
				if(edge==null) {
					Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(),a.getPeso());
				}
				else {
					int pv = (int) this.grafo.getEdgeWeight(edge);
					this.grafo.setEdgeWeight(edge, a.getPeso());
					
				}
			}
		}
		
	//	System.out.println(this.grafo);
		
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
		public List<Arco> getConnessi(){
			List<Arco> l = new LinkedList<Arco>();
	
				for(Arco a : this.dao.getArchi()) {
					if(this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
						l.add(new Arco(a.getA1(), a.getA2(),a.getPeso()));
					}
			   
				 
			}
				Collections.sort(l);
				   return l;
		 }
}
