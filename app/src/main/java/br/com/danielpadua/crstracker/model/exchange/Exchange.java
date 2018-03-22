package br.com.danielpadua.crstracker.model.exchange;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.pair.Pair;

/**
 * Created by danielpadua on 13/02/2018.
 */

public abstract class Exchange {

    private Date lastUpdated;

    public abstract int getId();

    public abstract Pair getBitcoinPair();

    public abstract List<Pair> getSupportedPairs();

    public abstract String getName();

    public abstract List<TickerType> getSupportedTickerTypes();

    public Pair getPairById(final int pairId) {
        return Iterables.find(getSupportedPairs(), new Predicate<Pair>() {
            @Override
            public boolean apply(@Nullable Pair input) {
                return input != null && input.getId() == pairId;
            }
        });
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public abstract int getRefreshCooldownSeconds();

    @Override
    public String toString() {
        return getName();
    }
}
