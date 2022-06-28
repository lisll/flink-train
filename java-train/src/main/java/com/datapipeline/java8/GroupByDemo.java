package com.datapipeline.java8;

import javax.xml.bind.SchemaOutputResolver;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.Collectors;

public class GroupByDemo {
  public static void main(String[] args) {
    BlogPost blogPost = new BlogPost("title1", "author1", BlogPost.BlogPostType.NEWS, 1,new Date(1656393771000L));
    BlogPost blogPost2 = new BlogPost("title2", "author1", BlogPost.BlogPostType.GUIDE, 2,new Date(1656393771299L));
    BlogPost blogPost3 = new BlogPost("title3", "author3", BlogPost.BlogPostType.NEWS, 3,new Date());
    BlogPost blogPost4 = new BlogPost("title1", "author3", BlogPost.BlogPostType.REVIEW, 4,new Date());
    BlogPost blogPost5 = new BlogPost("title2", "author4", BlogPost.BlogPostType.NEWS, 5,new Date());
    BlogPost blogPost6 = new BlogPost("title1", "author1", BlogPost.BlogPostType.REVIEW, 6,new Date());
    BlogPost blogPost7 = new BlogPost("title1", "author1", BlogPost.BlogPostType.NEWS, 2,new Date());
    BlogPost blogPost8 = new BlogPost("title1", "author1", BlogPost.BlogPostType.REVIEW, 6,new Date());
    BlogPost blogPost9 = new BlogPost("title1", "author1", BlogPost.BlogPostType.REVIEW, 10,new Date());
    BlogPost blogPost10 = new BlogPost("title1", "author1", BlogPost.BlogPostType.REVIEW, 8,new Date(1656393819737L));
    List<BlogPost> posts =
        Arrays.asList(
            blogPost,
            blogPost2,
            blogPost3,
            blogPost4,
            blogPost5,
            blogPost6,
            blogPost7,
            blogPost8,
            blogPost9,
            blogPost10);
    Map<BlogPost.BlogPostType, List<BlogPost>> collect =
        posts.stream().collect(groupingBy(BlogPost::getType));
    collect.forEach(
        (k, v) -> {
          System.out.println("key: " + k + "->" + v.size());
        });
    Map<BlogPost, List<BlogPost>> postsPerTypeAndAuthor =
        posts.stream().collect(groupingBy(post -> new BlogPost()));
    Map<BlogPost.BlogPostType, Set<BlogPost>> postsPerType =
        posts.stream().collect(groupingBy(BlogPost::getType, toSet()));

    Map<BlogPost.BlogPostType, Long> likesPerType =
        posts.stream().collect(groupingBy(BlogPost::getType, counting()));
    likesPerType.forEach(
        (k, v) -> {
          System.out.println("k: " + k + " -> " + v);
        });
    Map<BlogPost.BlogPostType, Optional<BlogPost>> maxLikesPerPostType =
        posts.stream()
            .collect(groupingBy(BlogPost::getType, maxBy(comparingInt(BlogPost::getLikes))));
    maxLikesPerPostType.forEach(
        (k, v) -> {
          System.out.println("kk: " + k + ",vv: " + v);
        });


      Map<Date, LongSummaryStatistics> likeStatisticsPerType = posts.stream()
              .collect(groupingBy(BlogPost::getDate,
                      summarizingLong(s->s.getDate().getTime())));
      likeStatisticsPerType.forEach((k,v)->{
          long max = v.getMax();
          long min = v.getMin();
          long count = v.getCount();
          System.out.println("max->"+max+" min->"+min+" count->"+count);
          System.out.println("mm->"+k+",nn->"+count);
      });

      Map<BlogPost.BlogPostType, String> postsPerType1 = posts.stream()
               .collect(groupingBy(BlogPost::getType,
                      mapping(BlogPost::getTitle, joining(", ", "Post titles: [", "]"))));

      Map<BlogPost.BlogPostType, Map<String, Object>> postsPerTypeList = posts.stream()
              .collect(groupingBy(BlogPost::getType, collectingAndThen(toList(), m->{
                  Map<String, Object> map = new HashMap<>();
                  m.stream().forEach(s-> System.out.println(s));
                  map.put("count", m.stream().count());
                  System.out.println("m.stream().count()->"+m.stream().count());
                  //对分组的list求和
                  map.put("money", m.stream().mapToDouble(BlogPost::getLikes).sum());
                  return map;
              })));


      Map<Date, Object> collect1 = posts.stream().collect(groupingBy(BlogPost::getDate, collectingAndThen(toList(), f -> {
          // 求他的最大值
          Long aLong = f.stream().map(s -> s.getDate().getTime()).collect(toList()).stream().max((o1, o2) -> 1).orElse(0L);
          f.stream().forEach(s-> System.out.println(">>>>>>>>>>>"+s));
          System.out.println("::::::"+f.stream().count());
          Map<String, Object> map = new HashMap<>();
          map.put("count",f.stream().count());
          return map;
      })));


      Map<Long, Long> demo =
              posts.stream().collect(groupingBy(s->s.getDate().getTime(), counting()));
      ArrayList<Map.Entry<Long, Long>> entries = new ArrayList<>(demo.entrySet());
      Collections.sort(entries,(k1,k2)-> (int) (k2.getKey()-k1.getKey()));
      System.out.println("最大值为->"+entries.get(0).getValue());

//      List<Map.Entry<Date,Integer>> list = new ArrayList(collect1.entrySet());
//      Collections.sort(list,(o1,o2)-> (int) (o1.getKey().getTime() - o2.getKey().getTime()));
//      Integer value = list.get(0).getValue();

  }
}
