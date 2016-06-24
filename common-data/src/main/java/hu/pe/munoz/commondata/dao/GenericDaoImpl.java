package hu.pe.munoz.commondata.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDaoImpl<E> implements GenericDao<E> {
    
    @PersistenceContext
    protected EntityManager em;
    
    private Class<E> entity;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        entity = (Class) pt.getActualTypeArguments()[0];
    }
    
    @Override
    public E insert(final E e) {
        em.persist(e);
        return e;
    }

    @Override
    public E update(final E e) {
        return em.merge(e);
    }

    @Override
    public void deleteById(final Object id) {
        em.remove(em.getReference(entity, id));
        em.flush();
    }

    @Override
    public void delete(final E e) {
        em.remove(e);
        em.flush();
    }

    @Override
    public E findById(final Object id) {
        return em.find(entity, id);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<E> findAll() {
        return em.createQuery("FROM " + entity.getName()).getResultList();
    }
}
