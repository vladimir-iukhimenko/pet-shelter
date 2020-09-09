package ru.pet.shelter.repository.utils;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.ConvertingParameterAccessor;
import org.springframework.data.mongodb.repository.query.ReactiveMongoQueryMethod;
import org.springframework.data.mongodb.repository.query.ReactivePartTreeMongoQuery;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class RegardingInheritanceMongoQuery extends ReactivePartTreeMongoQuery {
    private final Criteria inheritanceCriteria;

    public RegardingInheritanceMongoQuery(ReactiveMongoQueryMethod method, ReactiveMongoOperations mongoOperations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        super(method, mongoOperations, expressionParser, evaluationContextProvider);

        inheritanceCriteria =
                method.getEntityInformation().getJavaType().isAnnotationPresent(TypeAlias.class)
                        ? where("_class")
                        .is(method.getEntityInformation().getJavaType().getAnnotation(TypeAlias.class).value())
                        : null;
    }

    @Override
    protected Query createQuery(ConvertingParameterAccessor accessor) {
        Query query = super.createQuery(accessor);
        if (inheritanceCriteria != null) {
            query.addCriteria(inheritanceCriteria);
        }
        return query;
    }

    @Override
    protected Query createCountQuery(ConvertingParameterAccessor accessor) {
        Query query = super.createCountQuery(accessor);
        if (inheritanceCriteria != null) {
            query.addCriteria(inheritanceCriteria);
        }
        return query;
    }
}
