package org.sonar.core.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.sonar.test.DbTests;

import static org.assertj.core.api.Assertions.assertThat;

@Category(DbTests.class)
public class IsAliveMapperTest {

  @ClassRule
  public static DbTester dbTester = new DbTester();

  DbSession session;
  IsAliveMapper underTest;

  @Before
  public void setUp() throws Exception {
    session = dbTester.myBatis().openSession(false);
    underTest = session.getMapper(IsAliveMapper.class);
  }

  @After
  public void tearDown() throws Exception {
    session.close();
  }

  @Test
  public void isAlive_works_for_current_vendors() throws Exception {
    assertThat(underTest.isAlive()).isEqualTo(IsAliveMapper.IS_ALIVE_RETURNED_VALUE);
  }
}
