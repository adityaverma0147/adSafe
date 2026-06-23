package com.rtbplatform.common.config;

import com.rtbplatform.advertiser.entity.Advertiser;
import com.rtbplatform.advertiser.repository.AdvertiserRepository;
import com.rtbplatform.budget.entity.Budget;
import com.rtbplatform.budget.repository.BudgetRepository;
import com.rtbplatform.campaign.entity.Campaign;
import com.rtbplatform.campaign.entity.CampaignStatus;
import com.rtbplatform.campaign.repository.CampaignRepository;
import com.rtbplatform.user.entity.User;
import com.rtbplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, AdvertiserRepository advertiserRepository, CampaignRepository campaignRepository, BudgetRepository budgetRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setEmail("admin@adsafe.com");
                user.setPasswordHash("hashed_password");
                userRepository.save(user);

                Advertiser advertiser = new Advertiser();
                advertiser.setUser(user);
                advertiser.setCompanyName("AdSafe Global");
                advertiserRepository.save(advertiser);

                Campaign c1 = new Campaign();
                c1.setAdvertiser(advertiser);
                c1.setName("Summer Sale Retargeting");
                c1.setStatus(CampaignStatus.ACTIVE);
                c1.setStartDate(Instant.now());
                campaignRepository.save(c1);

                Budget b1 = new Budget();
                b1.setCampaign(c1);
                b1.setTotalLimit(new BigDecimal("5000.00"));
                b1.setDailyLimit(new BigDecimal("500.00"));
                b1.setTotalSpent(new BigDecimal("3450.00"));
                b1.setDailySpent(new BigDecimal("120.00"));
                budgetRepository.save(b1);

                Campaign c2 = new Campaign();
                c2.setAdvertiser(advertiser);
                c2.setName("Brand Awareness Q3");
                c2.setStatus(CampaignStatus.PAUSED);
                c2.setStartDate(Instant.now());
                campaignRepository.save(c2);

                Budget b2 = new Budget();
                b2.setCampaign(c2);
                b2.setTotalLimit(new BigDecimal("10000.00"));
                b2.setDailyLimit(new BigDecimal("1000.00"));
                b2.setTotalSpent(new BigDecimal("10000.00"));
                b2.setDailySpent(new BigDecimal("1000.00"));
                budgetRepository.save(b2);
            }
        };
    }
}
