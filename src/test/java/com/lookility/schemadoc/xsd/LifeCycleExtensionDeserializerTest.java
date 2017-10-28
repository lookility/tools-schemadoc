package com.lookility.schemadoc.xsd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

public class LifeCycleExtensionDeserializerTest {

    @Test
    public void getXmlSchema() {
        InputStream xsd = LifeCycleExtensionDeserializer.getSchema();

        assertNotNull(xsd);
    }
}
