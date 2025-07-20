package org.springframework.grpc.sample.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.grpc.sample.application.port.out.BankAccountCachePort;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisBankAccountCachePortAdapter implements BankAccountCachePort {

    private static final String CACHE_KEY_PREFIX = "bank_account:";

    private final RedisTemplate<String, BankAccount> redisTemplate;

    @Override
    public Optional<BankAccount> getBankAccountById(String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(buildCacheKey(id)));
    }

    @Override
    public void putBankAccount(BankAccount account) {
        redisTemplate.opsForValue().set(buildCacheKey(account.getId().toString()), account);
    }

    @Override
    public void evictBankAccount(String id) {
        redisTemplate.delete(buildCacheKey(id));
    }

    private static String buildCacheKey(String id) {
        return CACHE_KEY_PREFIX + id;
    }
}
