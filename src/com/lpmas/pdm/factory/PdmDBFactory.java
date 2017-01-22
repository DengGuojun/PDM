package com.lpmas.pdm.factory;

import java.sql.SQLException;

import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.db.MysqlDBExecutor;
import com.lpmas.framework.db.MysqlDBObject;
import com.lpmas.pdm.config.PdmDBConfig;

public class PdmDBFactory extends DBFactory {

	public DBObject getDBObjectR() throws SQLException {
		return new MysqlDBObject(PdmDBConfig.DB_LINK_PDM_R);
	}

	public DBObject getDBObjectW() throws SQLException {
		return new MysqlDBObject(PdmDBConfig.DB_LINK_PDM_W);
	}

	@Override
	public DBExecutor getDBExecutor() {
		return new MysqlDBExecutor();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
}
