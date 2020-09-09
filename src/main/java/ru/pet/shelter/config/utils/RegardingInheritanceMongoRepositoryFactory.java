package ru.pet.shelter.config.utils;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.query.ReactiveMongoQueryMethod;
import org.springframework.data.mongodb.repository.query.ReactiveStringBasedMongoQuery;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Optional;

public class RegardingInheritanceMongoRepositoryFactory extends ReactiveMongoRepositoryFactory {
    private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    private final ReactiveMongoOperations operations;

    public RegardingInheritanceMongoRepositoryFactory(ReactiveMongoOperations mongoOperations) {
        super(mongoOperations);
        this.operations = mongoOperations;
    }


    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable Key key,
                                                                   QueryMethodEvaluationContextProvider evaluationContextProvider) {
        return Optional.of(new MongoQueryLookupStrategy(operations, evaluationContextProvider,
                operations.getConverter().getMappingContext()));
    }

    private static class MongoQueryLookupStrategy implements QueryLookupStrategy {
        private final ReactiveMongoOperations operations;
        private final QueryMethodEvaluationContextProvider evaluationContextProvider;
        private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
            ReactiveMongoQueryMethod queryMethod = new ReactiveMongoQueryMethod(method, metadata, factory, this.mappingContext);
            String namedQueryName = queryMethod.getNamedQueryName();
            if (namedQueries.hasQuery(namedQueryName)) {
                String namedQuery = namedQueries.getQuery(namedQueryName);
                return new ReactiveStringBasedMongoQuery(namedQuery, queryMethod, this.operations, EXPRESSION_PARSER, this.evaluationContextProvider);
            } else if (queryMethod.hasAnnotatedQuery()) {
                return new ReactiveStringBasedMongoQuery(queryMethod, this.operations, EXPRESSION_PARSER, this.evaluationContextProvider);
            } else {
                return new RegardingInheritanceMongoQuery(queryMethod, this.operations, EXPRESSION_PARSER, this.evaluationContextProvider);
            }
        }

        MongoQueryLookupStrategy(ReactiveMongoOperations operations, QueryMethodEvaluationContextProvider evaluationContextProvider, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
            this.operations = operations;
            this.evaluationContextProvider = evaluationContextProvider;
            this.mappingContext = mappingContext;
        }
    }
}
