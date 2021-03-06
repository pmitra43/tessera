package com.quorum.tessera.test.rest;

import com.quorum.tessera.config.CommunicationType;
import com.quorum.tessera.test.*;
import org.junit.runner.RunWith;
import suite.SocketType;
import suite.TestSuite;

@RunWith(TestSuite.class)
@TestSuite.ProcessConfig(
        communicationType = CommunicationType.REST,
        dbType = DBType.SQLITE,
        socketType = SocketType.HTTP)
public class RestSuiteSqlite extends RestSuite {

}
