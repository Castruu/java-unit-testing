package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals("Erro de comparação", 1, 1);
        Assert.assertEquals(0.51234, 0.512,  0.001); // Delta é a margem de erro da comparação
        Assert.assertEquals(Math.PI, 3.14, 0.01); // Delta é a margem de erro da comparação

        int i = 5;
        Integer i2 = 5;
        Assert.assertEquals(Integer.valueOf(i), i2);
        Assert.assertEquals(i, i2.intValue());

        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "casa");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        Usuario u1 = new Usuario("Usuário 1");
        Usuario u2 = new Usuario("Usuário 1");
        Usuario u3 = null;

        Assert.assertEquals(u1, u2);

        // Assert.assertSame(u1, u2); - False -> Apenas mesma instância
        Assert.assertNotSame(u1, u2); // Diferentes instâncias

        Assert.assertSame(u1, u1);  // Apenas mesma instância

        Assert.assertNull(u3);
        Assert.assertNotNull(u1);
    }

}
