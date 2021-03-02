package com.minh.reddit.service;

import com.minh.reddit.dto.VoteDto;
import com.minh.reddit.exceptions.PostNotFoundException;
import com.minh.reddit.exceptions.SpringRedditException;
import com.minh.reddit.model.Post;
import com.minh.reddit.model.Vote;
import com.minh.reddit.repository.PostRepository;
import com.minh.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.minh.reddit.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

//    @Transactional
//    public void vote(VoteDto voteDto) {
//
//        Post post = postRepository.findById(voteDto.getPostId())
//                .orElseThrow(() -> new PostNotFoundException("Post not found with ID - " + voteDto.getPostId()));
//        Optional<Vote> voteByPostAndUser =
//                voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
//        if (voteByPostAndUser.isPresent()
//                && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
//            throw new SpringRedditException("You have already " + voteDto.getVoteType() + " 'd for this post");
//        }
//        if (UPVOTE.equals(voteDto.getVoteType())) {
//            post.setVoteCount(post.getVoteCount() + 1);
//        } else {
//            post.setVoteCount(post.getVoteCount() - 1);
//        }
//        voteRepository.save(mapToVote(voteDto, post));
//        postRepository.save(post);
//    }
//
//    private Vote mapToVote(VoteDto voteDto, Post post) {
//        return Vote.builder()
//                .voteType(voteDto.getVoteType())
//                .post(post)
//                .user(authService.getCurrentUser())
//                .build();
//    }

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

}

