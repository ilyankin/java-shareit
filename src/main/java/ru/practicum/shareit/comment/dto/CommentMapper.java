package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public final class CommentMapper {
    public static Comment toComment(CommentDtoIn commentDtoIn) {
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());
        return comment;
    }

    public static List<CommentDtoOut> toCommentDtos(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDtoOut).collect(Collectors.toList());
    }

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemName(comment.getItem().getName())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
