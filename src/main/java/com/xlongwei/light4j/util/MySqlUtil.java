package com.xlongwei.light4j.util;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.ConnectionSourceHelper;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.service.SingletonServiceFactory;
import com.zaxxer.hikari.HikariDataSource;

/**
 * mysql util
 * <li>apijson使用Java风格命名表字段，因此beetlsql也使用默认的DefaultNameConversion
 * @author xlongwei
 */
public class MySqlUtil {
	public static final HikariDataSource DATASOURCE = (HikariDataSource)SingletonServiceFactory.getBean(DataSource.class);
	public static final QueryRunner QUERYRUNNER = new QueryRunner(DATASOURCE);
	public static final SQLManager SQLMANAGER = new SQLManager(new MySqlStyle(), new ClasspathLoader("/beetl/sql"), ConnectionSourceHelper.getSingle(DATASOURCE));
	public static final Interceptor[] INTERS = new Interceptor[]{new DebugInterceptor()}, EMPTY_INTERS = new Interceptor[] {};
	private static final Logger log = LoggerFactory.getLogger(MySqlUtil.class);
	
	static {
		if(!SQLMANAGER.isProductMode()) {
			SQLMANAGER.setInters(INTERS);
		}
		TaskUtil.addShutdownHook(DATASOURCE);
	}
	
	/** QueryRunner回调 */
	@FunctionalInterface
	public interface QueryRunnerCallback<T> {
		/**
		 * 操作QueryRunner，必要时返回值
		 * @param qr
		 * @return
		 * @throws Exception
		 */
		T doInQueryRunner(QueryRunner qr) throws Exception;
		
		public static class Query<T> implements QueryRunnerCallback<T>  {
			private String sql;
			private ResultSetHandler<T> rsh;
			private Object[] params;
			public Query(String sql, ResultSetHandler<T> rsh, Object... params) {
				this.sql = sql;
				this.rsh = rsh;
				this.params = params;
			}
			@Override
			public T doInQueryRunner(QueryRunner qr) throws Exception {
				return qr.query(sql, rsh, params);
			}
		}
		
		public static class Insert<T> implements QueryRunnerCallback<T>  {
			private String sql;
			private ResultSetHandler<T> rsh;
			private Object[] params;
			public Insert(String sql, ResultSetHandler<T> rsh, Object... params) {
				this.sql = sql;
				this.rsh = rsh;
				this.params = params;
			}
			@Override
			public T doInQueryRunner(QueryRunner qr) throws Exception {
				boolean isBatch = params!=null && params.length>0 && params[0].getClass().isArray();
				if(isBatch) {
					int length = params.length;
					Object[][] batchParams = new Object[length][];
					for(int i=0; i<length; i++) {
						batchParams[i] = (Object[])params[i];
					}
					return qr.insertBatch(sql, rsh, batchParams);
				}else {
					return qr.insert(sql, rsh, params);
				}
			}
		}
		
		public static class Update implements QueryRunnerCallback<Integer>  {
			private String sql;
			private Object[] params;
			public Update(String sql, Object... params) {
				this.sql = sql;
				this.params = params;
			}
			@Override
			public Integer doInQueryRunner(QueryRunner qr) throws Exception {
				return qr.update(sql, params);
			}
		}
	}
	
	public static <T> T execute(QueryRunnerCallback<T> callback) {
		try {
			return callback.doInQueryRunner(QUERYRUNNER);
		}catch(Exception e) {
			log.warn("mysql query fail: {} {}", e.getClass().getSimpleName(), e.getMessage());
			return null;
		}
	}
}
