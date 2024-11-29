package com.ifpe.model.dao;

import com.ifpe.model.negocio.LoteProduto;
import com.ifpe.model.negocio.Mercadinho;
import com.ifpe.model.negocio.ONG;
import java.sql.Date;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerDao {

    private static ManagerDao myself = null;

    private EntityManagerFactory manager = null;

    private ManagerDao() {
        this.manager = Persistence.createEntityManagerFactory("maiscommenosPU");
    }

    public static ManagerDao getCurrentInstance() {
        if (myself == null) {
            myself = new ManagerDao();
        }
        return myself;
    }

    public void insert(Object o) {
        EntityManager em = manager.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.flush();
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
        em.close();
    }

    public <T> T readById(int id, Class<T> clazz) {
        EntityManager em = manager.createEntityManager();
        return em.find(clazz, id);
    }

    public <T> List<T> readAll(String jpql, Class<T> clazz) {
        EntityManager em = manager.createEntityManager();
        Query query = em.createQuery(jpql, clazz);
        return query.getResultList();
    }

    public List readProdutosByMercadinho(String query, Class c) {

        EntityManager em = manager.createEntityManager();

        List returnedList = em.createQuery(query, c).getResultList();

        em.close();

        return returnedList;
    }

    public void update(Object o) {
        EntityManager em = manager.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(o);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(Object o) {
        EntityManager em = manager.createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.contains(o) ? o : em.merge(o));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public <T> List<T> readWithParameters(String jpql, Class<T> clazz, Map<String, Object> parameters) {
        EntityManager em = manager.createEntityManager();
        Query query = em.createQuery(jpql, clazz);

        // Adiciona os parâmetros à consulta
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

    public List<Mercadinho> autenticarMercadinho(String login, String senha) {
        String query = "select m from Mercadinho m where m.login = :login and m.senha = :senha";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);
        parameters.put("senha", senha);
        return readWithParameters(query, Mercadinho.class, parameters);
    }

    public List<ONG> autenticarONG(String login, String senha) {
        String query = "select o from ONG o where o.login = :login and o.senha = :senha";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);
        parameters.put("senha", senha);
        return readWithParameters(query, ONG.class, parameters);
    }

    public static void main(String args[]) {
        
        List<Mercadinho> mercadinhos = getCurrentInstance().readAll("select m from Mercadinho m", Mercadinho.class);

        if ( mercadinhos.size()>0) {
            LoteProduto l = new LoteProduto();
            Mercadinho m = new Mercadinho();
            
            l.setMarca("Emoções");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Arroz");
            l.setMercadinho(m);
            l.setPrecoFinal(50.0);
            l.setPrecoInicial(15.0);
            l.setQuantidade("10");
            l.setValidade(data(1));
            l.setLote("123456");
            getCurrentInstance().insert(l);

            l.setMarca("Turquesa");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Feijão");
            l.setMercadinho(m);
            l.setPrecoFinal(80.0);
            l.setPrecoInicial(50.0);
            l.setQuantidade("10");
            l.setValidade(data(3));
            l.setLote("124578");
            getCurrentInstance().insert(l);

            l.setMarca("Vitarella");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Macarrão");
            l.setMercadinho(m);
            l.setPrecoFinal(25.0);
            l.setPrecoInicial(10.0);
            l.setQuantidade("10");
            l.setValidade(data(2));
            l.setLote("321456");
            getCurrentInstance().insert(l);

            l.setMarca("Biju");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Arroz");
            l.setMercadinho(m);
            l.setPrecoFinal(40.0);
            l.setPrecoInicial(10.0);
            l.setQuantidade("10");
            l.setValidade(data(-1));
            l.setLote("987456");
            getCurrentInstance().insert(l);

            l.setMarca("Ninho");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Leite em pó");
            l.setMercadinho(m);
            l.setPrecoFinal(60.0);
            l.setPrecoInicial(25.0);
            l.setQuantidade("10");
            l.setValidade(data(-2));
            l.setLote("543216");

            getCurrentInstance().insert(l);

            l.setMarca("Friboi");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Carne vermelha");
            l.setMercadinho(m);
            l.setPrecoFinal(60.0);
            l.setPrecoInicial(25.0);
            l.setQuantidade("10");
            l.setValidade(data(3));
            l.setLote("874596");

            getCurrentInstance().insert(l);
        } else {

            Mercadinho m = new Mercadinho();
            m.setCnpj("01234567890123");
            m.setContato("81997450185");
            m.setEndereco("Av Dr. josé Rufino, s/n");
            m.setNome("Mix Mateus");
            m.setLogin("mix");
            m.setSenha("1");
            
            getCurrentInstance().insert(m);
            
            m.setCnpj("789456123012345");
            m.setContato("81987799700");
            m.setEndereco("Av Doutor Carneiro Leão, 22");
            m.setNome("Boa Opção");
            m.setLogin("boa");
            m.setSenha("1");
            
            getCurrentInstance().insert(m);
            
            ONG o = new ONG();
            o.setContato("98732165498");
            o.setEndereco("rua liberdade, 101");
            o.setLogin("ong");
            o.setNome("Amor Fraterno");
            o.setSenha("1");
            
            getCurrentInstance().insert(o);
            
            o.setContato("98451289654");
            o.setEndereco("rua das moças, 400");
            o.setLogin("cuidar");
            o.setNome("Cuidar do Outro");
            o.setSenha("1");
            
            getCurrentInstance().insert(o);
            
            LoteProduto l = new LoteProduto();
            
            l.setMarca("Emoções");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Arroz");
            l.setMercadinho(m);
            l.setPrecoFinal(50.0);
            l.setPrecoInicial(15.0);
            l.setQuantidade("10");
            l.setValidade(data(1));
            l.setLote("123456");
            getCurrentInstance().insert(l);

            l.setMarca("Turquesa");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Feijão");
            l.setMercadinho(m);
            l.setPrecoFinal(80.0);
            l.setPrecoInicial(50.0);
            l.setQuantidade("10");
            l.setValidade(data(3));
            l.setLote("124578");
            getCurrentInstance().insert(l);

            l.setMarca("Vitarella");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Macarrão");
            l.setMercadinho(m);
            l.setPrecoFinal(25.0);
            l.setPrecoInicial(10.0);
            l.setQuantidade("10");
            l.setValidade(data(2));
            l.setLote("321456");
            getCurrentInstance().insert(l);

            l.setMarca("Biju");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Arroz");
            l.setMercadinho(m);
            l.setPrecoFinal(40.0);
            l.setPrecoInicial(10.0);
            l.setQuantidade("10");
            l.setValidade(data(-1));
            l.setLote("987456");
            getCurrentInstance().insert(l);

            l.setMarca("Ninho");
            m = ManagerDao.getCurrentInstance().readById(1, Mercadinho.class);
            l.setProduto("Leite em pó");
            l.setMercadinho(m);
            l.setPrecoFinal(60.0);
            l.setPrecoInicial(25.0);
            l.setQuantidade("10");
            l.setValidade(data(-2));
            l.setLote("543216");

            getCurrentInstance().insert(l);

            l.setMarca("Friboi");
            m = ManagerDao.getCurrentInstance().readById(2, Mercadinho.class);
            l.setProduto("Carne vermelha");
            l.setMercadinho(m);
            l.setPrecoFinal(60.0);
            l.setPrecoInicial(25.0);
            l.setQuantidade("10");
            l.setValidade(data(3));
            l.setLote("874596");

            getCurrentInstance().insert(l);
            
        }

    }

    public static Date data(int n) {
        Date hoje = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(hoje);
        c.add(Calendar.DATE, n);

        hoje = new java.sql.Date(c.getTimeInMillis());
        return hoje;
    }
}
