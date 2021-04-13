import java.util.*;
import java.util.function.*;

public class Operator<TItem, TCollection extends Collection<TItem>> {
    private TCollection collection;

    private Operator(TCollection collection) {
        this.collection = collection;
    }

    public static <TItem, TCollection extends Collection<TItem>> Operator<TItem, TCollection> modify(TCollection collection) {
        return new Operator<>(collection);
    }

    public Operator<TItem, TCollection> add(TItem element) {
        collection.add(element);
        return this;
    }

    public Operator<TItem, TCollection> add(Collection<TItem> elements) {
        collection.addAll(elements);
        return this;
    }

    public Operator<TItem, TCollection> remove(Predicate<? super TItem> predicate) {
        collection.removeIf(predicate);
        return this;
    }

    public Operator<TItem, TCollection> sort(Comparator<? super TItem> comparator) {
        var list = new ArrayList<>(collection);
        list.sort(comparator);
        collection = (TCollection) list;
        return this;
    }

    public Operator<TItem, TCollection> each(Consumer<? super TItem> selector) {
        for(TItem element : collection) {
            selector.accept(element);
        }
        return this;
    }

    public <TNewCollection extends Collection<TItem>> Operator<TItem, TNewCollection> copyTo(Supplier<TNewCollection> collectionCreator) {
        var newCollection = collectionCreator.get();
        newCollection.addAll(collection);
        return modify(newCollection);
    }

    public <TOut, TNewCollection extends Collection<TOut>> Operator<TOut, TNewCollection> convertTo(Supplier<TNewCollection> collectionCreator, Function<TItem, TOut> selector) {
        var newCollection = collectionCreator.get();
        for(TItem element : collection) {
            newCollection.add(selector.apply(element));
        }
        return modify(newCollection);
    }

    public TCollection get() {
        return collection;
    }
}
