package app.persistence;

import app.entities.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialMapperIntegrationTest {

    private static MaterialMapper materialMapper;

    @BeforeAll
    static void setup() {
        String user = "postgres";

        String password = System.getenv("JDBC_PASSWORD");

        String url = System.getenv("JDBC_CONNECTION_STRING");
        String db = "carport";

        ConnectionPool connectionPool = ConnectionPool.getInstance(user, password, url, db);
        materialMapper = new MaterialMapper(connectionPool);
    }


    // Tests that Java can connect to the database and get materials from the materials table
    @Test
    void testGetAllMaterialsReturnsMaterials() {
        List<Material> materials = materialMapper.getAllMaterials();

        assertNotNull(materials);
        assertFalse(materials.isEmpty());
    }


    // Tests that a material can be found by its id
    @Test
    void testGetMaterialByIdReturnsCorrectMaterial() {
        List<Material> materials = materialMapper.getAllMaterials();

        assertFalse(materials.isEmpty());

        Material firstMaterial = materials.get(0);
        Material foundMaterial = materialMapper.getMaterialById(firstMaterial.getMaterialId());

        assertNotNull(foundMaterial);
        assertEquals(firstMaterial.getMaterialId(), foundMaterial.getMaterialId());
        assertEquals(firstMaterial.getName(), foundMaterial.getName());
    }
}