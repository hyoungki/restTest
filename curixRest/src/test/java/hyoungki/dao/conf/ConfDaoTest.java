package hyoungki.dao.conf;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import hyoungki.conf.dao.ConfDaoJdbc;
import hyoungki.conf.domain.Conf;

public class ConfDaoTest {
	
	private ConfDaoJdbc dao;
	
	private Conf	conf1;
	private Conf	conf2;
	private Conf	conf3;
	
	@Before
	public void setUp() {
		
		dao	= new ConfDaoJdbc();
		SingleConnectionDataSource	dataSource	= new SingleConnectionDataSource();
		
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl("jdbc:sqlserver://192.168.1.59:1433;DatabaseName=CURIX55_LHK");
		dataSource.setUsername("sa");
		dataSource.setPassword("1234");
		dataSource.setSuppressClose(true);
		
		dao.setDataSource(dataSource);
		
		this.conf1	= new Conf("3EF9FD4C-7745-4B78-807C-5BC34D0C6C9B", "형기회의01", "user01", "20170710000000");
		this.conf2	= new Conf("3EF9FD4C-7745-4B78-807C-5BC34D0C6C9B", "형기회의02", "user02", "20170810000000");
		this.conf3	= new Conf("3EF9FD4C-7745-4B78-807C-5BC34D0C6C9B", "형기회의03", "user03", "20170910000000");
	}

	@Test
	public void andAndGet() {
		
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(conf1);
		dao.add(conf2);
		assertThat(dao.getCount(), equalTo(2));
		
		Conf		confget1	= dao.get(conf1.getCreatorUcid());
		assertThat(confget1.getTitle(), equalTo(conf1.getTitle()));
		assertThat(confget1.getCreatorUcid(), equalTo(conf1.getCreatorUcid()));
		
		Conf		confget2	= dao.get(conf2.getCreatorUcid());
		assertThat(confget2.getTitle(), equalTo(conf2.getTitle()));
		assertThat(confget2.getCreatorUcid(), equalTo(conf2.getCreatorUcid()));
		assertThat(confget2.getCreatorUcid(), equalTo(conf2.getCreatorUcid()));
	}
	
	@Test
	public void count() {

		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(conf1);
		assertThat(dao.getCount(), equalTo(1));
		
		dao.add(conf2);
		assertThat(dao.getCount(), equalTo(2));

		dao.add(conf3);
		assertThat(dao.getCount(), equalTo(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() {

		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.get("unknown_id");
	}
	
	@Test
	public void getAll() {
		
		dao.deleteAll();
		
		List<Conf>		confs0	= dao.getAll();
		assertThat(confs0.size(), equalTo(0));
		
		dao.add(conf1);
		List<Conf>		confs1	= dao.getAll();
		assertThat(confs1.size(), equalTo(1));
		checkSameConf(conf1, confs1.get(0));
		
		dao.add(conf2);
		List<Conf>		confs2	= dao.getAll();
		assertThat(confs2.size(), equalTo(2));
		checkSameConf(conf1, confs2.get(0));
		checkSameConf(conf2, confs2.get(1));
		
		dao.add(conf3);
		List<Conf>		confs3	= dao.getAll();
		assertThat(confs3.size(), equalTo(3));
		checkSameConf(conf1, confs3.get(0));
		checkSameConf(conf2, confs3.get(1));
		checkSameConf(conf3, confs3.get(2));
	}
	
	private void checkSameConf(Conf conf1, Conf conf2) {
		assertThat(conf1.getCreatorUcid(), equalTo(conf2.getCreatorUcid()));
		assertThat(conf1.getTitle(), equalTo(conf1.getTitle()));
		assertThat(conf1.getResvDate(), equalTo(conf1.getResvDate()));
	}
	
	@Test(expected = DataAccessException.class)
	public void duplciateKey() {
		dao.deleteAll();
		
		dao.add(conf1);
		dao.add(conf1);
	}
}