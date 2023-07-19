package example.micronaut

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.validation.Valid
import com.mongodb.client.model.Filters.eq


@Singleton // <1>
open class MongoDbFruitRepository(
    private val mongoConf: MongoDbConfiguration,  // <2>
    private val mongoClient: MongoClient) : FruitRepository { // <3>

    override fun save(@Valid fruit: Fruit): Mono<Boolean> =
        Mono.from(collection.insertOne(fruit)) // <4>
                .map { true }
                .onErrorReturn(false)

    @NonNull
    override fun list(): Publisher<Fruit> = collection.find() // <4>

    @NonNull
    override fun getByName(fruitName: String): Publisher<Fruit> {
        return collection.find(eq("name", fruitName))
    }

    private val collection: MongoCollection<Fruit>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection(mongoConf.collection, Fruit::class.java)
}
