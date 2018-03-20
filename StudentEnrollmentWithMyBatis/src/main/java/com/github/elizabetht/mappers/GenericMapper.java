package com.github.elizabetht.mappers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


public class GenericMapper<T extends Mappable> {

	private SqlSessionFactory ssf = null;
	private Class<T> clazz;
	
	private GenericMapper(SqlSessionFactory sqlSessionFactory){
		this.ssf = sqlSessionFactory;
	}
	
	public int insert(T o) {
		int id = -1;
		SqlSession session = ssf.openSession();
		try {
		id = session.insert(prefix() + ".insert", o);
		}finally {
			session.commit();
			session.close();
		}
		return id;
	}
	
	public void update(T o) {
	      SqlSession session = ssf.openSession();
	      try {
	         session.update(prefix() + ".update", o);
	      } finally {
	          session.commit();
	          session.close();
	      }
	}
	
    public void delete(T o){
    	 
        SqlSession session = ssf.openSession();
 
        try {
            session.delete(prefix() + ".delete", o.getUniqueID());
        } finally {
            session.commit();
            session.close();
        }

 
    }
    
    public List<T> selectAll(){
        List<T> list = null;
        SqlSession session = ssf.openSession();
 
        try {
            list = session.selectList(prefix() +".selectAll");
        } finally {
            session.close();
        }
        return list;
 
    }

   public void selectById(int id){
        SqlSession session = ssf.openSession();
        try {
            session.selectOne(prefix() + ".selectById", id);
        } finally {
            session.close();
        }
    } 

   public static <T extends Mappable> GenericMapper<T> mapperFromClass(SqlSessionFactory ssf,Class<T> clazz){
	   GenericMapper<T> gm = new GenericMapper<T>(ssf);
	   gm.clazz = clazz;
	   return gm;
   }
	
   private String prefix() {
	   return clazz.getSimpleName();
   }
}
