package com.sll.spring.cloud.ribbon;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class RibbonConfig {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    /**自定义配置ribbon负载均衡算法
     * @return
     * RoundRobinRule 轮询
     * RandomRule 随机
     * AvailabilityFilteringRule 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数超过阈值的服务，然后对剩余的服务列表进行轮询
     * WeightedResponseTimeRule 权重 根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。刚启动时，如果统计信息不足，则使用轮询策略，等信息足够，切换到 WeightedResponseTimeRule
     * RetryRule 重试 先按照轮询策略获取服务，如果获取失败则在指定时间内重试，获取可用服务
     * BestAvailableRule 选过滤掉多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
     * ZoneAvoidanceRule 符合判断server所在区域的性能和server的可用性选择服务
     */
    @Bean
    public IRule myRule(){
        //return new RoundRobinRule();//轮询
        //return new RetryRule();//重试
        return new BestAvailableRule();
    }





}
