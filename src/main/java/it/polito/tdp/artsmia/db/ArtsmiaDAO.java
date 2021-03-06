package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		System.out.println();
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRole(){
		String sql = "select distinct role " + 
				"from authorship " + 
				"order by role ASC" ;
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Integer> getArtisti(String r){
		String sql = "select distinct artist_id " + 
				"from authorship " + 
				"where role = ?" ;
		
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, r);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getInt("artist_id"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Arco> getArchi (String r){
		String sql =  "select a1.`artist_id` as ar1 ,e1.`object_id`, a2.`artist_id` as ar2, e2.`object_id`, count(e1.`exhibition_id`) as cnt " + 
				"from exhibition_objects as e1, exhibition_objects as e2, authorship as a1, authorship as a2  " + 
				"where e1.`exhibition_id` = e2.`exhibition_id` and e1.`object_id`!=e2.`object_id` and a1.`role`=? and a2.`role`=? and a1.`object_id`=e1.`object_id` and a2.`object_id`=e2.`object_id` and a1.`artist_id`>a2.`artist_id` " + 
				"group by a1.`artist_id`, e1.`object_id`, a2.`artist_id`, e2.`object_id` " ;
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, r);
			st.setString(2, r);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(new Arco(res.getInt("ar1"), res.getInt("ar2"), res.getInt("cnt")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		/**
		 * "select a1.`artist_id` as ar1 ,e1.`object_id`, a2.`artist_id` as ar2, e2.`object_id`, count(e1.`exhibition_id`) as cnt " + 
				"from exhibition_objects as e1, exhibition_objects as e2, authorship as a1, authorship as a2 " + 
				"where e1.`exhibition_id` = e2.`exhibition_id` and e1.`object_id`!=e2.`object_id` and a1.`object_id`=e1.`object_id` and a2.`object_id`=e2.`object_id` and a1.`artist_id`<a2.`artist_id` " + 
				"group by a1.`artist_id`, e1.`object_id`, a2.`artist_id`, e2.`object_id`"
		 */
	}
	
	
	//Punto d di un altro esame
	
	public String  getMaxNumeroOggetti(int anno) {
		String s = "";
		String sql = "select e.`exhibition_id` as id, COUNT(distinct e.`object_id`) as cnt " + 
				"from exhibition_objects as e , exhibitions as e1 " + 
				"where e.`exhibition_id` = e1.`exhibition_id` and e1.`begin` <= ? and e1.`end`>=? " + 
				"group by e.`exhibition_id` " + 
				"order by cnt DESC " + 
				"limit 1" ;
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery();
			if (res.next()) {

				s = "max" +  res.getInt("id") + res.getInt("cnt");
			}
			conn.close();
			return s;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return s;
		
	}
	
	public int getTrue(int id, String s) {
		String sql="select count(*) as cnt " + 
				"from authorship as a " + 
				"where a.`artist_id`=? and a.`role`='? " ;
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);
			st.setString(2, s);
			
			ResultSet res = st.executeQuery();
			if (res.next()) {
                 return res.getInt("cnt");
				
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return 0;
	}
	
	/**
	 * select e.exhibition_id, count(object_id) as counter 
	 * from exhibition_objects as eo,  exhibitions as e 
	 * where e.exhibition_id = eo.exhibition_id and begin >= ?
	   group by e.exhibition_id order by counter DESC LIMIT 1
				
				query prof
	 */
	
}
