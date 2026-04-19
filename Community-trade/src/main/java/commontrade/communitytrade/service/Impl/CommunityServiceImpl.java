package commontrade.communitytrade.service.impl;

import commontrade.communitytrade.mapper.CommunityMapper;
import commontrade.communitytrade.service.CommunityService;
import commontrade.commonyh.pojo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    // ====================== 帖子 ======================
    @Override
    public List<Post> getPostList() {
        return communityMapper.selectPosts();
    }

    @Override
    public boolean addPost(Post post) {
        return communityMapper.insertPost(post) > 0;
    }

    @Override
    public boolean deletePost(Integer id) {
        return communityMapper.deletePost(id) > 0;
    }

    @Override
    public boolean updatePost(Post post) {
        return communityMapper.updatePost(post) > 0;
    }

    // ====================== 评论 ======================
    @Override
    public List<CommentGood> getGoodCommentList(Integer id) {
        return communityMapper.selectCommentGood(id);
    }

    @Override
    public List<CommentUser> getUserCommentList(Integer id) {
        return communityMapper.selectCommentUser(id);
    }

    @Override
    public List<CommentPost> getPostCommentList(Integer id) {
        return communityMapper.selectCommentPost(id);
    }

    @Override
    public Post selectPostById(Integer id) {
        return communityMapper.selectPostById(id);
    }

    @Override
    public boolean deleteComment(String table, Integer id, Integer status) {
        return communityMapper.updateCommentDeleteStatus(table, id, status) > 0;
    }

    @Override
    public boolean reportComment(String table, Integer id) {
        return communityMapper.reportComment(table, id) > 0;
    }


    @Override
    public boolean insertCommentPost(CommentPost commentPost) {
        return communityMapper.insertCommentPost(commentPost) > 0;
    }

    @Override
    public boolean insertCommentGood(CommentGood commentGood) {
        return communityMapper.insertCommentGood(commentGood) > 0;
    }

    @Override
    public boolean insertCommentUser(CommentUser commentUser) {
        return communityMapper.insertCommentUser(commentUser) > 0;
    }
    // ====================== 点赞 / 收藏 ======================
    @Override
    public boolean toggleLike(Likes likes) {
        return communityMapper.toggleLike(likes) > 0;
    }

    @Override
    public boolean toggleFavorite(Favorite favorite) {
        return communityMapper.toggleFavorite(favorite) > 0;
    }
}